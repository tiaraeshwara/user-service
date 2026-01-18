package com.user_service.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.user_service.model.User;
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

    @RequestMapping("/getusers")
    public List<User> getUsers() {
        return userService.getUserInformation();
    }

    @RequestMapping("/getuserbyid/{id}")
    public User getUserById(@PathVariable Integer id) {
        return userService.getUserByUserId(id);
    }

    @RequestMapping("/getuserbyemail/{email}")
    public User getUserByEmail(@PathVariable String email) {
        return userService.getUserByUserEmail(email);
    }
}
