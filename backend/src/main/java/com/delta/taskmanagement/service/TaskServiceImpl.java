package com.delta.taskmanagement.service;

import com.delta.taskmanagement.dto.StatusUpdateRequest;
import com.delta.taskmanagement.dto.TaskCreateRequest;
import com.delta.taskmanagement.dto.TaskResponse;
import com.delta.taskmanagement.dto.TaskUpdateRequest;
import com.delta.taskmanagement.entity.Task;
import com.delta.taskmanagement.enums.Priority;
import com.delta.taskmanagement.enums.TaskStatus;
import com.delta.taskmanagement.exception.ResourceNotFoundException;
import com.delta.taskmanagement.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponse> getAllTasks(TaskStatus status, Priority priority, String keyword, String sortBy, String sortDirection) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection != null ? sortDirection : "DESC");
        String property = sortBy != null ? sortBy : "createdAt";
        Sort sort = Sort.by(direction, property);

        List<Task> tasks;

        if (keyword != null && !keyword.trim().isEmpty()) {
            tasks = taskRepository.searchTasks(status, priority, keyword.trim(), sort);
        } else if (status != null && priority != null) {
            tasks = taskRepository.findByStatusAndPriority(status, priority, sort);
        } else if (status != null) {
            tasks = taskRepository.findByStatus(status, sort);
        } else if (priority != null) {
            tasks = taskRepository.findByPriority(priority, sort);
        } else {
            tasks = taskRepository.findAll(sort);
        }

        return tasks.stream()
                .map(TaskResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public TaskResponse getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", id));
        return TaskResponse.fromEntity(task);
    }

    @Override
    @Transactional
    public TaskResponse createTask(TaskCreateRequest request) {
        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .priority(request.getPriority() != null ? request.getPriority() : Priority.MEDIUM)
                .status(TaskStatus.TODO)
                .build();

        Task savedTask = taskRepository.save(task);
        return TaskResponse.fromEntity(savedTask);
    }

    @Override
    @Transactional
    public TaskResponse updateTask(Long id, TaskUpdateRequest request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", id));

        if (!task.getVersion().equals(request.getVersion())) {
            throw new OptimisticLockingFailureException(
                    String.format("Version conflict: Task %d current version is %d, but request has %d",
                            id, task.getVersion(), request.getVersion()));
        }

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setPriority(request.getPriority());

        Task updatedTask = taskRepository.saveAndFlush(task);
        return TaskResponse.fromEntity(updatedTask);
    }

    @Override
    @Transactional
    public TaskResponse updateTaskStatus(Long id, StatusUpdateRequest request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", id));

        if (!task.getVersion().equals(request.getVersion())) {
            throw new OptimisticLockingFailureException(
                    String.format("Version conflict: Task %d current version is %d, but request has %d",
                            id, task.getVersion(), request.getVersion()));
        }

        task.setStatus(request.getStatus());

        Task updatedTask = taskRepository.saveAndFlush(task);
        return TaskResponse.fromEntity(updatedTask);
    }

    @Override
    @Transactional
    public void deleteTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", id));
        taskRepository.delete(task);
    }
}
