let rawBaseUrl = import.meta.env.VITE_API_BASE_URL || ''
if (rawBaseUrl && !rawBaseUrl.startsWith('http://') && !rawBaseUrl.startsWith('https://')) {
  rawBaseUrl = `https://${rawBaseUrl}`
}
const BASE_URL = rawBaseUrl

interface RequestOptions extends Omit<RequestInit, 'body'> {
  params?: Record<string, string | number | boolean | undefined>
  body?: unknown
}

export class HttpError extends Error {
  constructor(
    public status: number,
    public statusText: string,
    public data: unknown
  ) {
    super(`HTTP ${status}: ${statusText}`)
    this.name = 'HttpError'
  }
}

async function request<T>(endpoint: string, options: RequestOptions = {}): Promise<T> {
  const { params, body, headers: customHeaders, ...restOptions } = options

  const url = new URL(endpoint, BASE_URL || window.location.origin)
  if (params) {
    Object.entries(params).forEach(([key, value]) => {
      if (value !== undefined && value !== '') {
        url.searchParams.set(key, String(value))
      }
    })
  }

  const headers: HeadersInit = {
    'Content-Type': 'application/json',
    ...customHeaders,
  }

  const fetchOptions: RequestInit = {
    ...restOptions,
    headers,
    body: body ? JSON.stringify(body) : undefined,
  }

  const response = await fetch(url.toString(), fetchOptions)

  if (response.status === 204) {
    return undefined as T
  }

  // 伺服器異常防禦（避免 502/503 HTML 錯誤引發 JSON 解析中斷）
  let data: unknown
  const contentType = response.headers.get('content-type') || ''
  if (contentType.includes('application/json')) {
    data = await response.json()
  } else {
    data = await response.text()
  }

  if (!response.ok) {
    throw new HttpError(response.status, response.statusText, data)
  }

  return data as T
}

export const http = {
  get: <T>(url: string, params?: RequestOptions['params']) => request<T>(url, { method: 'GET', params }),
  post: <T>(url: string, body: unknown) => request<T>(url, { method: 'POST', body }),
  put: <T>(url: string, body: unknown) => request<T>(url, { method: 'PUT', body }),
  patch: <T>(url: string, body: unknown) => request<T>(url, { method: 'PATCH', body }),
  delete: <T = void>(url: string) => request<T>(url, { method: 'DELETE' }),
}