package com.example.soratech.controller;

import com.example.soratech.model.User;
import com.example.soratech.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequestMapping("/manager")
@PreAuthorize("hasAnyRole('Администратор', 'Менеджер')")
public class ManagerController {

    private final UserRepository userRepository;

    public ManagerController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public String managerPanel(@RequestParam(required = false) String entity, Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/";
        }

        User user = userRepository.findByEmail(principal.getName())
                .orElseGet(() -> userRepository.findByPhone(principal.getName()).orElse(null));

        model.addAttribute("user", user);

        if (entity != null) {
            model.addAttribute("activeEntity", entity);
        }
        return "manager/manager-panel";
    }
}


