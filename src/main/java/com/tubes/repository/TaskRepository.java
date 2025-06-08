package com.tubes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tubes.model.Project;
import com.tubes.model.User;
import com.tubes.model.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByProject(Project project);

    List<Task> findByAssignee(User assignee);
}