package com.delta.taskmanagement.service;

import com.delta.taskmanagement.dto.StatusUpdateRequest;
import com.delta.taskmanagement.dto.TaskCreateRequest;
import com.delta.taskmanagement.dto.TaskResponse;
import com.delta.taskmanagement.dto.TaskUpdateRequest;
import com.delta.taskmanagement.enums.Priority;
import com.delta.taskmanagement.enums.TaskStatus;

import java.util.List;

public interface TaskService {
    List<TaskResponse> getAllTasks(TaskStatus status, Priority priority, String keyword, String sortBy, String sortDirection);
    TaskResponse getTaskById(Long id);
    TaskResponse createTask(TaskCreateRequest request);
    TaskResponse updateTask(Long id, TaskUpdateRequest request);
    TaskResponse updateTaskStatus(Long id, StatusUpdateRequest request);
    void deleteTask(Long id);
}
