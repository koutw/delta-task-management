//[AI_assisted_001]
package com.delta.taskmanagement.dto;

import com.delta.taskmanagement.entity.Task;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private boolean completed;
    private Instant createdAt;
    private Instant expireAt;
    private Instant updatedAt;
    private Long version;

    public static TaskResponse fromEntity(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .completed(task.isCompleted())
                .createdAt(task.getCreatedAt())
                .expireAt(task.getExpireAt())
                .updatedAt(task.getUpdatedAt())
                .version(task.getVersion())
                .build();
    }
}
//[AI_assisted_001]