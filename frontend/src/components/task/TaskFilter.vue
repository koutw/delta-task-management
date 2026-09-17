<script setup lang="ts">
const filterState = defineModel<'ALL' | 'COMPLETED' | 'INCOMPLETE'>('filterState', { default: 'ALL' })
const emit = defineEmits<{ (e: 'open-create'): void }>()
</script>

<template>
  <div class="task-filter-container">
    <el-radio-group v-model="filterState">
      <el-radio-button value="ALL">全部</el-radio-button>
      <el-radio-button value="INCOMPLETE">未完成</el-radio-button>
      <el-radio-button value="COMPLETED">已完成</el-radio-button>
    </el-radio-group>
    <el-button type="primary" @click="emit('open-create')">新增任務</el-button>
  </div>
</template>

<style scoped>
.task-filter-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}
</style>