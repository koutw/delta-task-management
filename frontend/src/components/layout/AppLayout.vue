<template>
  <el-container class="app-layout">
    <el-header class="app-header">
      <div class="logo">任務管理系統</div>
    </el-header>
    <el-main class="app-main">
      <slot />
    </el-main>
  </el-container>
</template>

<style scoped>
.app-layout {
  min-height: 100vh;
  background-color: #f5f7fa;
}
.app-header {
  display: flex;
  align-items: center;
  background: #fff;
  border-bottom: 1px solid var(--el-border-color-light);
  padding: 0 24px;
}
.logo {
  font-weight: bold;
  font-size: 18px;
  color: #303133;
}
.app-main {
  padding: 24px;
  max-width: 1100px;
  margin: 0 auto;
  width: 100%;
}
</style>