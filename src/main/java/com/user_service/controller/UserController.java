package com.user_service.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.user_service.model.User;
import com.user_service.service.UserService;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/greet")
    public String greeting() {
        return userService.getGreeting();
    }

    @GetMapping("/enter-name")
    public String enterName() {
        return "Tiara Eshwara";
    }

    @GetMapping("/getusers")
    public List<User> getUsers() {
        return userService.getUserInformation();
    }

    @GetMapping("/getuserbyid/{id}")
    public User getUserById(@PathVariable Integer id) {
        return userService.getUserByUserId(id);
    }

    @GetMapping("/getuserbyemail/{email}")
    public User getUserByEmail(@PathVariable String email) {
        return userService.getUserByUserEmail(email);
    }

    @PostMapping("/add-user")
    public User addUser(@RequestBody User user) {
        return userService.addUser(user.getFirstName(), user.getLastName(), user.getAge(),
                user.getGender(), user.getCity(), user.getEmail(), user.getPhoneNumber());
    }

    @PutMapping("/update-user/{id}")
    public User updateUser(@PathVariable Integer id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    @DeleteMapping("/delete-user/{id}")
    public String deleteUser(@PathVariable Integer id) {
        boolean deleted = userService.deleteUser(id);
        if (deleted) {
            return "User deleted";
        }
        return "User not found";
    }
}
