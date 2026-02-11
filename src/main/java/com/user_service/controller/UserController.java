package com.user_service.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import com.user_service.model.User;
import com.user_service.service.UserService;

@Slf4j
@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/greet")
    public String greeting() {
        log.info("Greeting endpoint called");
        try {
            String response = userService.getGreeting();
            log.info("Greeting response generated successfully: {}", response);
            return response;
        } catch (Exception e) {
            log.error("Error generating greeting", e);
            throw e;
        }
    }

    @GetMapping("/enter-name")
    public String enterName() {
        log.info("Enter name endpoint called");
        String name = "Tiara Eshwara";
        log.info("Name returned: {}", name);
        return name;
    }

    @GetMapping("/getusers")
    public List<User> getUsers() {
        log.info("Get all users endpoint called");
        try {
            List<User> users = userService.getUserInformation();
            if (users != null && !users.isEmpty()) {
                log.info("Successfully retrieved {} users from database", users.size());
            } else {
                log.warn("No users found in database");
            }
            return users;
        } catch (Exception e) {
            log.error("Error occurred while fetching users", e);
            throw e;
        }
    }

    @GetMapping("/getuserbyid/{id}")
    public User getUserById(@PathVariable Integer id) {
        log.info("Get user by ID endpoint called with id: {}", id);
        try {
            if (id == null || id <= 0) {
                log.warn("Invalid user ID provided: {}", id);
                throw new IllegalArgumentException("User ID must be greater than 0");
            }
            User user = userService.getUserByUserId(id);
            if (user != null) {
                log.info("User found successfully for ID: {}", id);
            } else {
                log.warn("User not found for ID: {}", id);
            }
            return user;
        } catch (Exception e) {
            log.error("Error fetching user by ID: {}", id, e);
            throw e;
        }
    }

    @GetMapping("/getuserbyemail/{email}")
    public User getUserByEmail(@PathVariable String email) {
        log.info("Get user by email endpoint called with email: {}", email);
        try {
            if (email == null || email.isBlank()) {
                log.warn("Invalid email provided");
                throw new IllegalArgumentException("Email cannot be empty");
            }
            User user = userService.getUserByUserEmail(email);
            if (user != null) {
                log.info("User found successfully for email: {}", email);
            } else {
                log.warn("User not found for email: {}", email);
            }
            return user;
        } catch (Exception e) {
            log.error("Error fetching user by email: {}", email, e);
            throw e;
        }
    }

    @PostMapping("/add-user")
    public User addUser(@Valid @RequestBody User user) {
        log.info("Add user endpoint called with email: {}", user.getEmail());
        try {
            log.debug("Adding user details - First Name: {}, Last Name: {}, Email: {}",
                    user.getFirstName(), user.getLastName(), user.getEmail());

            User createdUser = userService.addUser(user.getFirstName(), user.getLastName(), user.getAge(),
                    user.getGender(), user.getCity(), user.getEmail(), user.getPhoneNumber());

            log.info("User created successfully with email: {}", user.getEmail());
            return createdUser;
        } catch (IllegalArgumentException e) {
            log.error("Validation error while adding user: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error while adding user with email: {}", user.getEmail(), e);
            throw e;
        }
    }

    @PutMapping("/update-user/{id}")
    public User updateUser(@PathVariable Integer id, @Valid @RequestBody User user) {
        log.info("Update user endpoint called with ID: {}", id);
        try {
            if (id == null || id <= 0) {
                log.warn("Invalid user ID provided for update: {}", id);
                throw new IllegalArgumentException("User ID must be greater than 0");
            }
            if (user == null) {
                log.warn("Null user object provided for updating user with ID: {}", id);
                throw new IllegalArgumentException("User object cannot be null");
            }
            log.debug("Updating user ID: {} with details - Email: {}, Name: {} {}",
                    id, user.getEmail(), user.getFirstName(), user.getLastName());

            User updatedUser = userService.updateUser(id, user);
            log.info("User updated successfully for ID: {} with email: {}", id, user.getEmail());
            return updatedUser;
        } catch (IllegalArgumentException e) {
            log.error("Validation error while updating user ID {}: {}", id, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error while updating user with ID: {}", id, e);
            throw e;
        }
    }

    @DeleteMapping("/delete-user/{id}")
    public String deleteUser(@PathVariable Integer id) {
        log.info("Delete user endpoint called with ID: {}", id);
        try {
            if (id == null || id <= 0) {
                log.warn("Invalid user ID provided for deletion: {}", id);
                throw new IllegalArgumentException("User ID must be greater than 0");
            }
            log.debug("Attempting to delete user with ID: {}", id);

            boolean deleted = userService.deleteUser(id);
            if (deleted) {
                log.info("User deleted successfully for ID: {}", id);
                return "User deleted";
            } else {
                log.warn("User not found for deletion with ID: {}", id);
                return "User not found";
            }
        } catch (IllegalArgumentException e) {
            log.error("Validation error while deleting user ID {}: {}", id, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error while deleting user with ID: {}", id, e);
            throw e;
        }
    }
}
