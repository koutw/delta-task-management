/**
 * 後端標準 API 錯誤回應結構 (對應 OpenAPI ErrorResponse 規格)
 */
export interface ApiErrorResponse {
  timestamp?: string
  status?: number
  error?: string
  message?: string
  path?: string
}

/**
 * HTTP Request 自訂配置參數
 */
export interface RequestOptions extends Omit<RequestInit, 'body'> {
  /**
   * URL Query 查詢參數
   */
  params?: Record<string, any>
  /**
   * Request Body (物件/陣列會自動被轉成 JSON 字串)
   */
  body?: any
  /**
   * 請求逾時毫秒數 (預設 10000ms)
   */
  timeout?: number
}
