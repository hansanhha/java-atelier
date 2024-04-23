package com.hansanhha.spring.security.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class OAuth2LoginController {

    @GetMapping("/login")
    public String login() {
        return "login-page";
    }

    @GetMapping("/hello")
    public String succeedLogin() {
        return "login-success";
    }
}
