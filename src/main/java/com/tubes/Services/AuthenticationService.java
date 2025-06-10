package com.tubes.Services;

import com.tubes.model.Project;
import com.tubes.model.Task;
import com.tubes.model.User;
import com.tubes.repository.ProjectRepository;
import com.tubes.repository.TaskRepository;
import com.tubes.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private ProjectRepository projectRepo;
    private TaskRepository taskRepo;

    public AuthenticationService(UserRepository userRepository, ProjectRepository projectRepo,
            TaskRepository taskRepo) {
        this.userRepository = userRepository;
        this.projectRepo = projectRepo;
        this.taskRepo = taskRepo;
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        String username = authentication.getName();
        return userRepository.findByUsername(username);
    }

    public boolean isCurrentUser(Long userId) {
        User currentUser = getCurrentUser();
        return currentUser != null && currentUser.getId().equals(userId);
    }

    // public boolean canEditTask(Long taskId) {
    // Task task = taskRepo.findById(taskId).orElse(null);
    // User currentUser = getCurrentUser();

    // if (task == null || currentUser == null) {
    // return false;
    // }

    // boolean isAssignee = task.getAssignee() != null &&
    // task.getAssignee().getId().equals(currentUser.getId());
    // boolean isProjectOwner = task.getProject().getOwner() != null &&
    // task.getProject().getOwner().getId().equals(currentUser.getId());
    // boolean isAdmin = currentUser.getRole().equals("ROLE_ADMIN");

    // return isAssignee || isProjectOwner || isAdmin;
    // }

    // public boolean isProjectOwner(Long projectId) {
    // Project project = projectRepo.findById(projectId).orElse(null);
    // User currentUser = getCurrentUser();

    // return project != null && currentUser != null &&
    // project.getOwner() != null &&
    // project.getOwner().getId().equals(currentUser.getId());
    // }
}