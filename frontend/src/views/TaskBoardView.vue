<template>
  <div class="task-board-container">
    <!-- 頁面頂部標題與新增按鈕 -->
    <div class="board-header">
      <h2>任務列表</h2>
      <el-button type="primary">
        新增任務
      </el-button>
    </div>

    <!-- 篩選列 -->
    <div class="filter-section">
      <TaskFilter />
    </div>

    <!-- 任務表格列表 -->
    <div class="table-section">
      <TaskTable />
    </div>

    <!-- 新增 / 編輯對話框 -->
    <TaskFormDialog />
  </div>
</template>

<script setup lang="ts">
import TaskFilter from '@/components/task/TaskFilter.vue'
import TaskTable from '@/components/task/TaskTable.vue'
import TaskFormDialog from '@/components/task/TaskFormDialog.vue'
</script>

<style scoped>
.task-board-container {
  padding: 20px;
}

.board-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.filter-section {
  margin-bottom: 20px;
}

.table-section {
  background: #ffffff;
}
</style>
