package com.web.crawler.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {

    @GetMapping
    public String viewLogin(Authentication auth) {
        if(auth != null) return "redirect:/welcome";
        return "login";
    }
}
