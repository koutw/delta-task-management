//[AI_assisted_001]
package com.delta.taskmanagement.repository;

import com.delta.taskmanagement.entity.Task;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByCompleted(Boolean completed, Sort sort);
}
//[AI_assisted_001]