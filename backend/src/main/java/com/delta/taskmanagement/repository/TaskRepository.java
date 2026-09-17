package com.delta.taskmanagement.repository;

import com.delta.taskmanagement.entity.Task;
import com.delta.taskmanagement.enums.Priority;
import com.delta.taskmanagement.enums.TaskStatus;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByStatus(TaskStatus status, Sort sort);
    List<Task> findByPriority(Priority priority, Sort sort);
    List<Task> findByStatusAndPriority(TaskStatus status, Priority priority, Sort sort);

    @Query("SELECT t FROM Task t WHERE " +
            "(:status IS NULL OR t.status = :status) AND " +
            "(:priority IS NULL OR t.priority = :priority) AND " +
            "(:keyword IS NULL OR LOWER(t.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(t.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Task> searchTasks(@Param("status") TaskStatus status,
                           @Param("priority") Priority priority,
                           @Param("keyword") String keyword,
                           Sort sort);
}
