<template>
  <div class="dashboard-container">
    <!-- 頁面標題 -->
    <div class="dashboard-header">
      <h2>統計儀表板</h2>
    </div>

    <!-- 圖表區塊 (ElRow + ElCol + ElCard) -->
    <el-row :gutter="20">
      <el-col :xs="24" :sm="12">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>任務狀態分布</span>
            </div>
          </template>
          <StatusChart />
        </el-card>
      </el-col>

      <el-col :xs="24" :sm="12">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>任務優先級分布</span>
            </div>
          </template>
          <PriorityChart />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import StatusChart from '@/components/dashboard/StatusChart.vue'
import PriorityChart from '@/components/dashboard/PriorityChart.vue'
</script>

<style scoped>
.dashboard-container {
  padding: 20px;
}

.dashboard-header {
  margin-bottom: 20px;
}

.card-header {
  font-weight: bold;
}
</style>
