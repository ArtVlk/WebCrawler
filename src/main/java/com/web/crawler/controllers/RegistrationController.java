package com.web.crawler.controllers;

import com.web.crawler.entities.User;
import com.web.crawler.services.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

@Controller
@RequestMapping("/registration")
@RequiredArgsConstructor
public class RegistrationController {

    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService customUserDetailsService;

    @GetMapping
    public String viewRegistration() {
        return "registration";
    }

    @PostMapping
    public String addUser(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String email
    ) {
        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .createdDate(new Date())
                .build();

        if(customUserDetailsService.addUser(user)) {
            return "redirect:/login";
        }

        return "registration";
    }
}
