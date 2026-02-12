package com.user_service.service;

import java.util.List;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.user_service.dao.UserDao;
import com.user_service.model.User;

@Service("UserService")
public class UserService {
    private final UserDao userDao;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public String getGreeting() {
        return userDao.getGreetingDao();
    }

    public List<User> getUserInformation() {
        return userDao.getUserInformation();
    }

    public User getUserByUserId(int id) {
        return userDao.getUserByUserId(id);
    }

    public User getUserByUserEmail(String email) {
        return userDao.getUserByUserEmail(email);
    }

    public User addUser(String firstName, String lastName, Integer age, String gender, String city, String email,
            String phoneNumber) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setAge(age);
        user.setGender(gender);
        user.setCity(city);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);

        validateUser(user);

        return userDao.addUser(user);
    }

    private void validateUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User must be provided");
        }
        if (user.getFirstName() == null || user.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("firstName must be provided");
        }
        if (user.getLastName() == null || user.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("lastName must be provided");
        }
        if (user.getEmail() == null || user.getEmail().trim().isEmpty() || !EMAIL_PATTERN.matcher(user.getEmail()).matches()) {
            throw new IllegalArgumentException("A valid email must be provided");
        }
        if (user.getAge() != null && user.getAge() < 0) {
            throw new IllegalArgumentException("age must be non-negative");
        }
        if (user.getPhoneNumber() != null) {
            String digits = user.getPhoneNumber().replaceAll("\\D", "");
            if (digits.length() < 7 || digits.length() > 15) {
                throw new IllegalArgumentException("phoneNumber must contain between 7 and 15 digits");
            }
        }
    }

    /**
     * Simple login: authenticate by email + phoneNumber match.
     * Returns matched User or null on failure.
     */
    public User login(String email, String phoneNumber) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("email must be provided");
        }
        User existing = userDao.getUserByUserEmail(email);
        if (existing == null || existing.getPhoneNumber() == null) {
            return null;
        }
        String existingDigits = existing.getPhoneNumber().replaceAll("\\D", "");
        String providedDigits = phoneNumber == null ? "" : phoneNumber.replaceAll("\\D", "");
        if (existingDigits.equals(providedDigits)) {
            return existing;
        }
        return null;
    }

    public User updateUser(Integer id, User user) {
        if (id == null) {
            throw new IllegalArgumentException("id must be provided");
        }
        return userDao.updateUser(id, user);
    }

    public boolean deleteUser(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("id must be provided");
        }
        User existing = userDao.getUserByUserId(id);
        if (existing == null) {
            return false;
        }
        return userDao.deleteUser(id);
    }
}
