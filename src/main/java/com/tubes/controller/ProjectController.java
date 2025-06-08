package com.tubes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.tubes.model.Project;
import com.tubes.repository.ProjectRepository;

@Controller
@RequestMapping("/projects")
public class ProjectController {
    @Autowired
    private ProjectRepository projectRepo;

    @GetMapping
    public String listProjects(Model model) {
        model.addAttribute("projects", projectRepo.findAll());
        return "project/list";
    }

    @GetMapping("/new")
    public String newProject(Model model) {
        model.addAttribute("project", new Project());
        return "project/form";
    }

    @PostMapping
    public String createProject(@ModelAttribute Project project) {
        projectRepo.save(project);
        return "redirect:/projects";
    }

    @GetMapping("/{id}")
    public String viewProject(@PathVariable Long id, Model model) {
        model.addAttribute("project", projectRepo.findById(id).orElse(null));
        return "project/view";
    }

    @GetMapping("/{id}/edit")
    public String editProject(@PathVariable Long id, Model model) {
        model.addAttribute("project", projectRepo.findById(id).orElse(null));
        return "project/form";
    }

    @PostMapping("/{id}")
    public String updateProject(@PathVariable Long id, @ModelAttribute Project project) {
        project.setId(id);
        projectRepo.save(project);
        return "redirect:/projects/" + id;
    }

    @PostMapping("/{id}/delete")
    public String deleteProject(@PathVariable Long id) {
        projectRepo.deleteById(id);
        return "redirect:/projects";
    }
}