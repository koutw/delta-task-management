package com.delta.taskmanagement.dto;

import com.delta.taskmanagement.enums.TaskStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StatusUpdateRequest {

    @NotNull
    private TaskStatus status;

    @NotNull
    private Long version;
}
