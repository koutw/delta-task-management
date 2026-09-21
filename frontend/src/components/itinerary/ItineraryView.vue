<script setup lang="ts">
import { ref } from 'vue'

const iframeUrl = ref('/gemini-code-1789986610358.html')
</script>

<template>
  <div class="itinerary-container">
    <iframe
      :src="iframeUrl"
      title="行程規劃"
      class="itinerary-frame"
    />
  </div>
</template>

<style scoped>
.itinerary-container {
  width: 100%;
  height: calc(100vh - 200px);
  min-height: 700px;
  background: #ffffff;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.06);
  border: 1px solid var(--el-border-color-light);
}

.itinerary-frame {
  width: 100%;
  height: 100%;
  border: none;
}
</style>
