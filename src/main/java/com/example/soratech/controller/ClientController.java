package com.example.soratech.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/client")
@PreAuthorize("hasAnyRole('Администратор', 'Менеджер', 'Клиент')")
public class ClientController {

    @GetMapping
    public String clientPanel() {
        // Перенаправление на профиль
        return "redirect:/profile";
    }
}


