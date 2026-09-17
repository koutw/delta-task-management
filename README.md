## 領域模型 (Domain Model)

專案領域核心Entity：

    `Task` 
    class Task {
        +Long id
        +String title
        +String description
        +boolean completed
        +LocalDateTime createdAtgit
        +LocalDateTime updatedAt
        +Long version
    }

### 領域模型欄位

| 欄位名稱 | 型態 | 說明 |
| :--- | :--- | :--- |
| `id` | `Long` | 任務唯一識別碼（Primary Key, Auto-Increment） |
| `title` | `String` | 任務標題（必填，上限 255 字元） |
| `description` | `String` | 任務詳細說明（選填，TEXT） |
| `completed` | `boolean` | 完成狀態（`true`: 已完成, `false`: 未完成，預設 `false`） |
| `createdAt` | `LocalDateTime` | 建立時間（自動產生） |
| `updatedAt` | `LocalDateTime` | 最後更新時間（自動產生） |
| `version` | `Long` | 樂觀鎖版本號（使用 JPA `@Version` 自動維護） |
---

## OpenAPI 規格存取說明

本專案使用 **OpenAPI 定義 RESTful API 規格。

## REST API Endpoint

| HTTP 方法 | 端點 (Endpoint) | 功能說明 | 成功狀態碼 |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/v1/tasks` | 查詢任務列表 (支援 `completed` 篩選) | `200 OK` |
| `POST` | `/api/v1/tasks` | 建立新任務 | `201 Created` |
| `GET` | `/api/v1/tasks/{id}` | 查詢單一任務詳細資訊 | `200 OK` |
| `PUT` | `/api/v1/tasks/{id}` | 更新任務內容與完成狀態 (含樂觀鎖驗證) | `200 OK` |
| `DELETE` | `/api/v1/tasks/{id}` | 刪除指定任務 | `204 No Content` |

---