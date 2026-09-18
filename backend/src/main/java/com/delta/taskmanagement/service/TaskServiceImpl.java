//[AI_assisted_001]
package com.delta.taskmanagement.service;

import com.delta.taskmanagement.dto.TaskRequest;
import com.delta.taskmanagement.dto.TaskResponse;
import com.delta.taskmanagement.entity.Task;
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
    public List<TaskResponse> getAllTasks(Boolean completed) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        List<Task> tasks = (completed != null)
                ? taskRepository.findByCompleted(completed, sort)
                : taskRepository.findAll(sort);

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
    public TaskResponse createTask(TaskRequest request) {
        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .completed(request.isCompleted())
                .expireAt(request.getExpireAt())
                .build();

        Task savedTask = taskRepository.save(task);
        return TaskResponse.fromEntity(savedTask);
    }

    @Override
    @Transactional
    public TaskResponse updateTask(Long id, TaskRequest request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", id));

        if (request.getVersion() != null && !task.getVersion().equals(request.getVersion())) {
            throw new OptimisticLockingFailureException(
                    String.format("Version conflict: Task %d current version is %d, but request has %d",
                            id, task.getVersion(), request.getVersion()));
        }

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setCompleted(request.isCompleted());
        task.setExpireAt(request.getExpireAt());

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
//[AI_assisted_001]