<script setup lang="ts">
import type { FormInstance, FormRules } from 'element-plus'
import { ref, reactive, watch } from 'vue'
import type { Task, TaskRequest } from '@/types/task'

const props = defineProps<{
  editingTask: Task | null
}>()

const emit = defineEmits<{
  'submit-create': [data: TaskRequest]
  'submit-update': [id: number, data: TaskRequest]
}>()

const visible = defineModel<boolean>({ default: false })
const formRef = ref<FormInstance>()

const formData = reactive({
  id: null as number | null,
  title: '',
  description: '',
  completed: false,
  expireAt: '' as string | undefined,
  version: 0,
})

const rules: FormRules = {
  title: [
    { required: true, message: '請輸入任務標題', trigger: 'blur' },
    { min: 1, max: 255, message: '標題長度需介於 1 至 255 個字元', trigger: 'blur' },
  ],
}

watch(
  () => props.editingTask,
  (task) => {
    if (task) {
      formData.id = task.id
      formData.title = task.title
      formData.description = task.description || ''
      formData.completed = task.completed
      formData.expireAt = task.expireAt || undefined
      formData.version = task.version
    } else {
      formData.id = null
      formData.title = ''
      formData.description = ''
      formData.completed = false
      formData.expireAt = undefined
      formData.version = 0
    }
    formRef.value?.clearValidate()
  },
  { immediate: true }
)

function handleConfirm() {
  formRef.value?.validate((valid) => {
    if (valid) {
      const payload: TaskRequest = {
        title: formData.title.trim(),
        description: formData.description.trim() || undefined,
        completed: formData.completed,
        expireAt: formData.expireAt || undefined,
        version: formData.version,
      }

      if (formData.id !== null) {
        emit('submit-update', formData.id, payload)
      } else {
        emit('submit-create', payload)
      }
      visible.value = false
    }
  })
}
</script>

<template>
  <el-dialog
    v-model="visible"
    :title="formData.id ? '編輯任務' : '新增任務'"
    width="500px"
    destroy-on-close
  >
    <el-form ref="formRef" :model="formData" :rules="rules" label-width="90px">
      <el-form-item label="標題" prop="title">
        <el-input v-model="formData.title" placeholder="請輸入任務標題" />
      </el-form-item>

      <el-form-item label="描述" prop="description">
        <el-input
          v-model="formData.description"
          type="textarea"
          :rows="3"
          placeholder="請輸入任務詳細說明"
        />
      </el-form-item>

      <el-form-item label="到期時間" prop="expireAt">
        <el-date-picker
          v-model="formData.expireAt"
          type="datetime"
          placeholder="請選擇到期時間"
          value-format="YYYY-MM-DDTHH:mm:ss[Z]"
          style="width: 100%;"
        />
      </el-form-item>

      <el-form-item v-if="formData.id !== null" label="完成狀態">
        <el-switch v-model="formData.completed" active-text="已完成" inactive-text="未完成" />
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="handleConfirm">確認儲存</el-button>
    </template>
  </el-dialog>
</template>