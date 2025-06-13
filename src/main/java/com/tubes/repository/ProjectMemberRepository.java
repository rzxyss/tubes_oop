package com.tubes.repository;

import com.tubes.model.ProjectMember;
import com.tubes.model.Project;
import com.tubes.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {
    List<ProjectMember> findByProject(Project project);
    List<ProjectMember> findByUser(User user);
    ProjectMember findByProjectAndUser(Project project, User user);
}