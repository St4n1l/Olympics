package com.example.Olympics.controller;

import org.springframework.web.bind.annotation.GetMapping;

public class RegistrationViewController
{   @GetMapping("/view")
    public String view() {
        return "register";
    }
}
