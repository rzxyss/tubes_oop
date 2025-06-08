package com.tubes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.tubes.model.Project;
import com.tubes.model.User;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByOwner(User owner);
}