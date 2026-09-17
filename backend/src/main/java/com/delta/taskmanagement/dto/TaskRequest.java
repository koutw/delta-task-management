package com.delta.taskmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TaskRequest {

    @NotBlank
    @Size(max = 255)
    private String title;

    private String description;

    private boolean completed = false;

    private Long version;
}
