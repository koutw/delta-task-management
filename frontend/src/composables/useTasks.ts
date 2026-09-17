import { ref, computed } from 'vue'
import { taskApi } from '@/api/taskApi'
import { HttpError } from '@/api/httpClient'
import { ElMessage } from 'element-plus'
import type { Task, TaskRequest } from '@/types/task'

export function useTasks() {
  const tasks = ref<Task[]>([])
  const loading = ref(false)
  const filterState = ref<'ALL' | 'COMPLETED' | 'INCOMPLETE'>('ALL')
  const updatingTaskIds = ref<Set<number>>(new Set())

  const filteredTasks = computed(() => {
    if (filterState.value === 'COMPLETED') return tasks.value.filter(t => t.completed)
    if (filterState.value === 'INCOMPLETE') return tasks.value.filter(t => !t.completed)
    return tasks.value
  })

  function isUpdating(id: number): boolean {
    return updatingTaskIds.value.has(id)
  }

  async function fetchTasks(): Promise<void> {
    loading.value = true
    try {
      tasks.value = await taskApi.getAll()
    } catch (err) {
      ElMessage.error('載入任務失敗')
    } finally {
      loading.value = false
    }
  }

  async function createTask(payload: TaskRequest): Promise<void> {
    try {
      const created = await taskApi.create(payload)
      tasks.value.unshift(created)
      ElMessage.success('任務建立成功')
    } catch (err) {
      ElMessage.error('建立任務失敗')
    }
  }

  async function updateTask(id: number, payload: TaskRequest): Promise<void> {
    try {
      const updated = await taskApi.update(id, payload)
      const index = tasks.value.findIndex(t => t.id === id)
      if (index !== -1) {
        tasks.value[index] = updated
      }
      ElMessage.success('任務更新成功')
    } catch (err) {
      if (err instanceof HttpError && err.status === 409) {
        ElMessage.error('資料版本衝突 (樂觀鎖)，已重新載入最新內容')
        await fetchTasks()
      } else {
        ElMessage.error('更新任務失敗')
      }
    }
  }

  async function toggleComplete(task: Task): Promise<void> {
    if (updatingTaskIds.value.has(task.id)) return

    const previousState = task.completed
    const targetState = !task.completed
    updatingTaskIds.value.add(task.id)

    // 樂觀更新
    task.completed = targetState

    try {
      const updated = await taskApi.update(task.id, {
        title: task.title,
        description: task.description,
        completed: targetState,
        version: task.version,
      })
      const index = tasks.value.findIndex(t => t.id === task.id)
      if (index !== -1) {
        tasks.value[index] = updated
      }
    } catch (err) {
      // 衝突時回滾
      task.completed = previousState
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

  async function deleteTask(id: number): Promise<void> {
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

  return {
    tasks,
    loading,
    filterState,
    filteredTasks,
    isUpdating,
    fetchTasks,
    createTask,
    updateTask,
    toggleComplete,
    deleteTask,
  }
}
