<script setup lang="ts">
import { ref, computed } from 'vue'
import type { Task } from '@/types/task'

const props = defineProps<{
  tasks: Task[]
  loading: boolean
  isUpdating: (id: number) => boolean
}>()
const emit = defineEmits<{
  toggle: [task: Task]
  edit: [task: Task]
  delete: [id: number]
  'create-new-task': []
}>()

const filterState = ref<'ALL' | 'COMPLETED' | 'INCOMPLETE'>('ALL')

const filteredTasks = computed(() => {
  if (filterState.value === 'COMPLETED') return props.tasks.filter(t => t.completed)
  if (filterState.value === 'INCOMPLETE') return props.tasks.filter(t => !t.completed)
  return props.tasks
})

function formatDate(dateString?: string) {
  if (!dateString) return '-'
  return new Date(dateString).toLocaleString('zh-TW', {
    timeZone: 'Asia/Taipei',
    hour12: false,
  })
}
</script>

<template>
  <div>
    <div class="container">
      <el-radio-group v-model="filterState">
        <el-radio-button value="ALL">全部</el-radio-button>
        <el-radio-button value="INCOMPLETE">未完成</el-radio-button>
        <el-radio-button value="COMPLETED">已完成</el-radio-button>
      </el-radio-group>
      <el-button type="primary" @click="emit('create-new-task')">新增任務</el-button>
    </div>

    <el-table :data="filteredTasks" v-loading="loading" border stripe style="width: 100%;">
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
          <el-tag :type="row.completed ? 'success' : 'info'" effect="light">
            {{ row.completed ? '已完成' : '未完成' }}
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column label="建立時間" width="180" align="center">
        <template #default="{ row }">
          {{ formatDate(row.createdAt) }}
        </template>
      </el-table-column>

      <el-table-column label="到期時間" width="180" align="center">
        <template #default="{ row }">
          <div :class="{ expired: row.expireAt && new Date(row.expireAt).getTime() < Date.now() }">
            {{ formatDate(row.expireAt) }}
          </div>
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
  </div>
</template>

<style scoped>
.container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}
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
.expired {
  color: #f56c6c;
  font-weight: 500;
}
</style>