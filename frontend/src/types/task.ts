export interface Task {
  id: number
  title: string
  description?: string
  completed: boolean
  createdAt: string
  expireAt?: string
  updatedAt: string
  version: number
}

export interface TaskRequest {
  title: string
  description?: string
  completed?: boolean
  expireAt?: string
  version?: number
}