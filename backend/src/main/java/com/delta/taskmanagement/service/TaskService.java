package com.delta.taskmanagement.service;

import com.delta.taskmanagement.dto.TaskRequest;
import com.delta.taskmanagement.dto.TaskResponse;

import java.util.List;

public interface TaskService {
    List<TaskResponse> getAllTasks(Boolean completed);
    TaskResponse getTaskById(Long id);
    TaskResponse createTask(TaskRequest request);
    TaskResponse updateTask(Long id, TaskRequest request);
    void deleteTask(Long id);
}
