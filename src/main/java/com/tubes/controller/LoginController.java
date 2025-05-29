package com.tubes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.tubes.model.User;
import com.tubes.repository.UserRepository;

@Controller
public class LoginController {

    @Autowired
    private UserRepository userRepo;

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("user", new User());
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute User user, Model model) {
        User u = userRepo.findByUsernameAndPassword(user.getUsername(), user.getPassword());
        if (u != null) {
            return "redirect:/";
        }
        model.addAttribute("error", "Username atau Password salah");
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user) {
        userRepo.save(user);
        return "redirect:/login";
    }
}