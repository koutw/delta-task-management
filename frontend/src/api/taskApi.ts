//[AI_assisted_002]
import type { Task, TaskRequest } from '@/types/task'

let BASE_URL = import.meta.env.VITE_API_BASE_URL || ''
if (BASE_URL && !BASE_URL.startsWith('http')) {
  BASE_URL = `https://${BASE_URL}`
}
const API_URL = `${BASE_URL}/api/v1/tasks`

export class HttpError extends Error {
  constructor(
    public status: number,
    message: string
  ) {
    super(message)
    this.name = 'HttpError'
  }
}

async function request<T>(url: string, options?: RequestInit): Promise<T> {
  const res = await fetch(url, {
    headers: { 'Content-Type': 'application/json' },
    ...options,
  })

  if (res.status === 204) {
    return undefined as T
  }

  const contentType = res.headers.get('content-type') || ''
  const data = contentType.includes('application/json')
    ? await res.json()
    : await res.text()

  if (!res.ok) {
    throw new HttpError(res.status, res.statusText)
  }
  return data as T
}

export const taskApi = {
  getAll: () => request<Task[]>(API_URL),
  create: (data: TaskRequest) => request<Task>(API_URL, { method: 'POST', body: JSON.stringify(data) }),
  update: (id: number, data: TaskRequest) =>
    request<Task>(`${API_URL}/${id}`, { method: 'PUT', body: JSON.stringify(data) }),
  delete: (id: number) => request<void>(`${API_URL}/${id}`, { method: 'DELETE' }),
}
//[AI_assisted_002]