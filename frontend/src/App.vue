<script setup lang="ts">
import { ref, onMounted } from 'vue'
import zhTw from 'element-plus/es/locale/lang/zh-tw'
import { ElMessage } from 'element-plus'
import type { Task, TaskRequest } from '@/types/task'
import { taskApi, HttpError } from '@/api/taskApi'
import AppLayout from '@/components/layout/AppLayout.vue'
import TaskTable from '@/components/task/TaskTable.vue'
import TaskFormDialog from '@/components/task/TaskFormDialog.vue'

const tasks = ref<Task[]>([])
const selectedTask = ref<Task | null>(null)

const loading = ref(false)

// 記錄哪些任務正在更新 防抖用
const updatingTaskIds = ref(new Set())
// 單一任務是否正在更新
function isUpdating(id: number): boolean {
  return updatingTaskIds.value.has(id)
}


const isDialogVisible = ref(false)

/** API */
async function fetchTasks() {
  loading.value = true

  try {
    tasks.value = await taskApi.getAll()
  } catch (err) {
    ElMessage.error('載入任務失敗')
  } finally {
    loading.value  = false
  }
}

async function createTask(payload: TaskRequest) {
  try {
    const created = await taskApi.create(payload)
    tasks.value.unshift(created)
    ElMessage.success('任務建立成功')
  } catch (err) {

    ElMessage.error('建立任務失敗')
  }
}

async function updateTask(id: number, payload: TaskRequest) {
  try {
    const updated = await taskApi.update(id, payload)
    const index = tasks.value.findIndex(t => t.id === id)
    if (index !== -1) {
      tasks.value[index] = updated
    }
    ElMessage.success('任務更新成功')
  } catch (err) {
    // 當版本衝突時
    if (err instanceof HttpError && err.status === 409) {
      ElMessage.error('資料版本衝突，已重新載入')
      await fetchTasks()
    } else {
      await fetchTasks()
      ElMessage.error('更新任務失敗')
    }
  }
}

// 切換狀態
async function toggleComplete(task: Task) {
  if (updatingTaskIds.value.has(task.id)) return

  const originalState = task.completed
  const targetState = !task.completed
  updatingTaskIds.value.add(task.id)

  task.completed = targetState

  // 嘗試更新狀態
  try {
    const updated = await taskApi.update(task.id, {
      title: task.title,
      description: task.description,
      completed: targetState,
      expireAt: task.expireAt,
      version: task.version,
    })
    const index = tasks.value.findIndex(t => t.id === task.id)
    if (index !== -1) {
      tasks.value[index] = updated
    }
  } catch (err) {
    // 如果衝突，回復畫面上的狀態
    task.completed = originalState
    if (err instanceof HttpError && err.status === 409) {
      ElMessage.error('版本衝突，已恢復狀態並重新載入')
      await fetchTasks()
    } else {
      ElMessage.error('更新完成狀態失敗')
    }
  } finally {
    updatingTaskIds.value.delete(task.id)
  }
}

async function deleteTask(id: number) {
  try {
    await taskApi.delete(id)
    tasks.value = tasks.value.filter(t => t.id !== id)
    ElMessage.success('任務已刪除')
  } catch (err) {
    if (err instanceof HttpError && err.status === 404) {
      tasks.value = tasks.value.filter(t => t.id !== id)
      ElMessage.info('此任務已不存在')
    } else {
      ElMessage.error('刪除失敗')
    }
  }
}

function openCreateDialog() {
  selectedTask.value = null
  isDialogVisible.value = true
}

function openEditDialog(task: Task) {
  selectedTask.value = { ...task }
  isDialogVisible.value = true
}

// 掛載完成後取得所有任務
onMounted(() => {
  fetchTasks()
})
</script>

<template>
  <el-config-provider :locale="zhTw">
    <AppLayout>
        <TaskTable
          :tasks="tasks"
          :loading="loading"
          :is-updating="isUpdating"
          @create-new-task="openCreateDialog"
          @toggle="toggleComplete"
          @edit="openEditDialog"
          @delete="deleteTask"
        />

      <TaskFormDialog
        v-model="isDialogVisible"
        :editing-task="selectedTask"
        @submit-create="createTask"
        @submit-update="updateTask"
      />
    </AppLayout>
  </el-config-provider>
</template>