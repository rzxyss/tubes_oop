package com.tubes.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String status;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate deadline;

    @ManyToOne
    private User owner;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectMember> members = new ArrayList<>();

    // Constructors
    public Project() {
    }

    public Project(String name, String description, String status, LocalDate deadline, User owner) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.deadline = deadline;
        this.owner = owner;
    }

    // Helper methods untuk mengelola members
    public void addMember(User user, String role) {
        ProjectMember member = new ProjectMember(this, user, role);
        members.add(member);
    }

    public void removeMember(User user) {
        members.removeIf(member -> member.getUser().getId().equals(user.getId()));
    }

    public boolean hasMember(User user) {
        return members.stream()
                .anyMatch(member -> member.getUser().getId().equals(user.getId()));
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public List<ProjectMember> getMembers() {
        return members;
    }

    public void setMembers(List<ProjectMember> members) {
        this.members = members;
    }
}
