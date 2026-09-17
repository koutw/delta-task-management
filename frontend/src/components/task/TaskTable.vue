<script setup lang="ts">
import type { Task } from '@/types/task'
import TaskStatusTag from './TaskStatusTag.vue'

defineProps<{
  tasks: Task[]
  loading: boolean
  isUpdating: (id: number) => boolean
}>()

const emit = defineEmits<{
  (e: 'toggle', task: Task): void
  (e: 'edit', task: Task): void
  (e: 'delete', id: number): void
}>()

function formatDate(iso: string) {
  if (!iso) return '-'
  return new Date(iso).toLocaleString('zh-TW', { hour12: false })
}
</script>

<template>
  <el-table :data="tasks" v-loading="loading" border stripe style="width: 100%;">
    <el-table-column label="完成" width="80" align="center">
      <template #default="{ row }">
        <el-switch
          :model-value="row.completed"
          :loading="isUpdating(row.id)"
          :disabled="isUpdating(row.id)"
          @change="() => emit('toggle', row)"
        />
      </template>
    </el-table-column>

    <el-table-column prop="title" label="任務內容" min-width="260">
      <template #default="{ row }">
        <div :class="{ 'completed-title': row.completed }">
          <div class="task-title">{{ row.title }}</div>
          <div v-if="row.description" class="task-desc">{{ row.description }}</div>
        </div>
      </template>
    </el-table-column>

    <el-table-column label="狀態" width="100" align="center">
      <template #default="{ row }">
        <TaskStatusTag :completed="row.completed" />
      </template>
    </el-table-column>

    <el-table-column label="建立時間" width="180" align="center">
      <template #default="{ row }">
        {{ formatDate(row.createdAt) }}
      </template>
    </el-table-column>

    <el-table-column label="操作" width="140" align="center" fixed="right">
      <template #default="{ row }">
        <el-button link type="primary" size="small" @click="emit('edit', row)">
          編輯
        </el-button>
        <el-popconfirm
          title="確定要刪除這筆任務嗎？"
          confirm-button-text="確定"
          cancel-button-text="取消"
          @confirm="emit('delete', row.id)"
        >
          <template #reference>
            <el-button link type="danger" size="small">刪除</el-button>
          </template>
        </el-popconfirm>
      </template>
    </el-table-column>

    <template #empty>
      <el-empty description="尚無任何任務" :image-size="120" />
    </template>
  </el-table>
</template>

<style scoped>
.task-title {
  font-weight: 500;
  color: var(--el-text-color-primary);
}
.task-desc {
  font-size: 13px;
  color: var(--el-text-color-secondary);
  margin-top: 4px;
}
.completed-title {
  text-decoration: line-through;
  opacity: 0.6;
}
</style>