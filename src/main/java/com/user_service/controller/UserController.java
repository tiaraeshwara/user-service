package com.user_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.user_service.service.UserService;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/greet")
    public String greeting() {
        return userService.getGreeting();
    }

    @RequestMapping("/enter-name")
    public String enterName() {
        return "Tiara Eshwara";
    }
}
