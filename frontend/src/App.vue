<script setup lang="ts">
import { ref, onMounted } from 'vue'
import zhTw from 'element-plus/es/locale/lang/zh-tw'
import type { Task, TaskRequest } from '@/types/task'
import { useTasks } from '@/composables/useTasks'
import AppLayout from '@/components/layout/AppLayout.vue'
import TaskFilter from '@/components/task/TaskFilter.vue'
import TaskTable from '@/components/task/TaskTable.vue'
import TaskFormDialog from '@/components/task/TaskFormDialog.vue'

const {
  loading,
  filterState,
  filteredTasks,
  isUpdating,
  fetchTasks,
  createTask,
  updateTask,
  toggleComplete,
  deleteTask,
} = useTasks()

const isDialogVisible = ref(false)
const selectedTask = ref<Task | null>(null)

function openCreateDialog() {
  selectedTask.value = null
  isDialogVisible.value = true
}

function openEditDialog(task: Task) {
  selectedTask.value = { ...task }
  isDialogVisible.value = true
}

function handleCreate(payload: TaskRequest) {
  createTask(payload)
}

function handleUpdate(id: number, payload: TaskRequest) {
  updateTask(id, payload)
}

onMounted(() => {
  fetchTasks()
})
</script>

<template>
  <el-config-provider :locale="zhTw">
    <AppLayout>
      <TaskFilter
        v-model:filterState="filterState"
        @open-create="openCreateDialog"
      />

      <TaskTable
        :tasks="filteredTasks"
        :loading="loading"
        :is-updating="isUpdating"
        @toggle="toggleComplete"
        @edit="openEditDialog"
        @delete="deleteTask"
      />

      <TaskFormDialog
        v-model="isDialogVisible"
        :editing-task="selectedTask"
        @submit-create="handleCreate"
        @submit-update="handleUpdate"
      />
    </AppLayout>
  </el-config-provider>
</template>