package com.tubes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.tubes.Services.AuthenticationService;
import com.tubes.model.User;
import com.tubes.repository.UserRepository;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private AuthenticationService authService;

    @GetMapping("")
    public String index(Model model) {
        model.addAttribute("users", userRepo.findAll());
        return "user/index";
    }

    @GetMapping("/new")
    public String newUser(Model model) {
        model.addAttribute("user", new User());
        return "user/form";
    }

    @PostMapping("/create")
    public String createUser(@ModelAttribute User user) {
        userRepo.save(user);
        return "redirect:/users";
    }

    @GetMapping("/{id}/edit")
    public String editUser(@PathVariable Long id, Model model) {
        User user = userRepo.findById(id).orElse(null);
        model.addAttribute("user", user);
        return "user/form";
    }

    @PostMapping("/{id}/update")
    public String updateUser(@PathVariable Long id, @ModelAttribute User user) {
        user.setId(id);
        userRepo.save(user);
        return "redirect:/users";
    }

    @PostMapping("/{id}/delete")
    public String deleteUser(@PathVariable Long id) {
        userRepo.deleteById(id);
        return "redirect:/users";
    }
}