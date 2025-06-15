package com.tubes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.tubes.Services.AuthenticationService;
import com.tubes.model.User;
import com.tubes.repository.UserRepository;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private UserRepository userRepo;
    
    @Autowired
    private AuthenticationService authService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public String viewProfile(Model model) {
        User currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", currentUser);
        return "profile/view";
    }

    @GetMapping("/edit")
    public String editProfile(Model model) {
        User currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", currentUser);
        return "profile/edit";
    }

    @PostMapping("/update")
    public String updateProfile(@ModelAttribute User user) {
        User currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            return "redirect:/login";
        }
        
        currentUser.setName(user.getName());
        currentUser.setEmail(user.getEmail());
        currentUser.setPhone(user.getPhone());
        
        userRepo.save(currentUser);
        return "redirect:/profile";
    }

    @GetMapping("/change-password")
    public String changePasswordForm(Model model) {
        return "profile/change-password";
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam String currentPassword,
                               @RequestParam String newPassword,
                               @RequestParam String confirmPassword,
                               Model model) {
        User currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            return "redirect:/login";
        }

        if (!passwordEncoder.matches(currentPassword, currentUser.getPassword())) {
            model.addAttribute("error", "Password saat ini tidak sesuai");
            return "profile/change-password";
        }

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "Password baru dan konfirmasi tidak cocok");
            return "profile/change-password";
        }

        currentUser.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(currentUser);

        return "redirect:/profile?passwordChanged=true";
    }
}