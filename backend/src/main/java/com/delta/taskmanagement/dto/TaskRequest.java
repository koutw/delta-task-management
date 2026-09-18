//[AI_assisted_001]
package com.delta.taskmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.Instant;

@Data
public class TaskRequest {

    @NotBlank
    @Size(max = 255)
    private String title;

    private String description;

    private boolean completed = false;

    private Instant expireAt;

    private Long version;
}
//[AI_assisted_001]