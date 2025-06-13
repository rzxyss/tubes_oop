package com.tubes.repository;

import com.tubes.model.ProjectMember;
import com.tubes.model.User;
import com.tubes.model.Project;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {
    List<ProjectMember> findByProject(Project project);
    void deleteByProject(Project project);
    List<ProjectMember> findByUser(User user);
}