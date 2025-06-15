package com.tubes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.tubes.model.*;
import com.tubes.repository.*;
import java.util.*;
import java.util.stream.Collectors;
import com.tubes.Services.AuthenticationService;

@Controller
@RequestMapping("/projects")
public class ProjectController {
    @Autowired
    private ProjectRepository projectRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ProjectMemberRepository projectMemberRepo;

    @Autowired
    private AuthenticationService authService;

    @GetMapping
    public String listProjects(Model model) {
        User currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            return "redirect:/login";
        }

        List<Project> projects;

        if (currentUser.getRole().equals("ROLE_ADMIN")) {
            projects = projectRepo.findAll();
        } else {
            Set<Project> userProjects = new HashSet<>();
            List<ProjectMember> memberships = projectMemberRepo.findByUser(currentUser);
            for (ProjectMember membership : memberships) {
                userProjects.add(membership.getProject());
            }
            userProjects.addAll(projectRepo.findByOwner(currentUser));
            projects = new ArrayList<>(userProjects);
        }

        model.addAttribute("projects", projects);
        return "project/list";
    }

    @GetMapping("/new")
    public String newProject(Model model) {
        model.addAttribute("project", new Project());
        model.addAttribute("users", userRepo.findAll());
        return "project/form";
    }

    @PostMapping("/create")
    public String createProject(@ModelAttribute Project project,
            @RequestParam(required = false) List<Long> memberIds) {
        // Save project first to get the ID
        Project savedProject = projectRepo.save(project);

        // Handle members
        if (memberIds != null && !memberIds.isEmpty()) {
            for (Long userId : memberIds) {
                User user = userRepo.findById(userId).orElse(null);
                if (user != null) {
                    ProjectMember member = new ProjectMember(savedProject, user, "MEMBER");
                    projectMemberRepo.save(member);
                }
            }
        }
        return "redirect:/projects";
    }

    @GetMapping("/{id}")
    public String viewProject(@PathVariable Long id, Model model) {
        Project project = projectRepo.findById(id).orElse(null);
        if (project == null) {
            return "redirect:/projects";
        }

        model.addAttribute("project", project);
        return "project/view";
    }

    @GetMapping("/{id}/edit")
    public String editProject(@PathVariable Long id, Model model) {
        Project project = projectRepo.findById(id).orElse(null);
        if (project == null) {
            return "redirect:/projects";
        }

        // Get selected member IDs
        List<Long> selectedMembers = project.getMembers().stream()
                .map(member -> member.getUser().getId())
                .collect(Collectors.toList());

        model.addAttribute("project", project);
        model.addAttribute("users", userRepo.findAll());
        model.addAttribute("selectedMembers", selectedMembers);
        return "project/form";
    }

    @PostMapping("/update")
    public String updateProject(@ModelAttribute Project project,
            @RequestParam(required = false) List<Long> memberIds) {
        // Update project
        Project updatedProject = projectRepo.save(project);

        // First remove all existing members
        projectMemberRepo.deleteByProject(updatedProject);

        // Then add new members
        if (memberIds != null && !memberIds.isEmpty()) {
            for (Long userId : memberIds) {
                User user = userRepo.findById(userId).orElse(null);
                if (user != null) {
                    ProjectMember member = new ProjectMember(updatedProject, user, "MEMBER");
                    projectMemberRepo.save(member);
                }
            }
        }

        return "redirect:/projects/" + updatedProject.getId();
    }

    @PostMapping("/{id}/delete")
    public String deleteProject(@PathVariable Long id) {
        projectRepo.deleteById(id);
        return "redirect:/projects";
    }
}