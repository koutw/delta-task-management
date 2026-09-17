<script setup lang="ts">
defineProps<{ completed: boolean }>()
</script>

<template>
  <el-tag :type="completed ? 'success' : 'info'" effect="light">
    {{ completed ? '已完成' : '未完成' }}
  </el-tag>
</template>
