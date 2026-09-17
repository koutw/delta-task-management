import { http } from './httpClient'
import type { Task, TaskRequest } from '@/types/task'

const ENDPOINT_PREFIX = '/api/v1/tasks'

export const taskApi = {
  getAll: (completed?: boolean) =>
    http.get<Task[]>(ENDPOINT_PREFIX, completed !== undefined ? { completed } : undefined),
  getById: (id: number) => http.get<Task>(`${ENDPOINT_PREFIX}/${id}`),
  create: (data: TaskRequest) => http.post<Task>(ENDPOINT_PREFIX, data),
  update: (id: number, data: TaskRequest) => http.put<Task>(`${ENDPOINT_PREFIX}/${id}`, data),
  delete: (id: number) => http.delete(`${ENDPOINT_PREFIX}/${id}`),
}