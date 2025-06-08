package com.tubes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.tubes.model.Project;
import com.tubes.model.Task;
import com.tubes.repository.ProjectRepository;
import com.tubes.repository.TaskRepository;

@Controller
@RequestMapping("/projects/{projectId}/tasks")
public class TaskController {
    @Autowired
    private TaskRepository taskRepo;
    @Autowired
    private ProjectRepository projectRepo;

    @GetMapping("/new")
    public String newTask(@PathVariable Long projectId, Model model) {
        Project project = projectRepo.findById(projectId).orElse(null);
        Task task = new Task();
        task.setProject(project);
        model.addAttribute("task", task);
        return "task/form";
    }

    @PostMapping
    public String createTask(@PathVariable Long projectId, @ModelAttribute Task task) {
        Project project = projectRepo.findById(projectId).orElse(null);
        task.setProject(project);
        taskRepo.save(task);
        return "redirect:/projects/" + projectId;
    }

    @GetMapping("/{id}")
    public String viewTask(@PathVariable Long projectId, @PathVariable Long id, Model model) {
        model.addAttribute("task", taskRepo.findById(id).orElse(null));
        return "task/view";
    }

    @GetMapping("/{id}/edit")
    public String editTask(@PathVariable Long projectId, @PathVariable Long id, Model model) {
        model.addAttribute("task", taskRepo.findById(id).orElse(null));
        return "task/form";
    }

    @PostMapping("/{id}")
    public String updateTask(@PathVariable Long projectId, @PathVariable Long id, @ModelAttribute Task task) {
        task.setId(id);
        Project project = projectRepo.findById(projectId).orElse(null);
        task.setProject(project);
        taskRepo.save(task);
        return "redirect:/projects/" + projectId;
    }

    @PostMapping("/{id}/delete")
    public String deleteTask(@PathVariable Long projectId, @PathVariable Long id) {
        taskRepo.deleteById(id);
        return "redirect:/projects/" + projectId;
    }

    @PostMapping("/{id}/status")
    public String updateStatus(@PathVariable Long projectId, @PathVariable Long id, @RequestParam String status) {
        Task task = taskRepo.findById(id).orElse(null);
        if (task != null) {
            task.setStatus(status);
            taskRepo.save(task);
        }
        return "redirect:/projects/" + projectId;
    }
}