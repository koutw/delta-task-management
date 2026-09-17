package com.delta.taskmanagement.dto;

import com.delta.taskmanagement.enums.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TaskCreateRequest {

    @NotBlank
    @Size(max = 255)
    private String title;

    private String description;

    private Priority priority = Priority.MEDIUM;
}
