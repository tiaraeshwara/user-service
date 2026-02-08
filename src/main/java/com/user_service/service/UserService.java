package com.user_service.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.user_service.dao.UserDao;
import com.user_service.model.User;

@Service("UserService")
public class UserService {
    private final UserDao userDao;

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
        return userDao.addUser(user);
    }

    public User updateUser(Integer id, User user) {
        if (id == null) {
            throw new IllegalArgumentException("id must be provided");
        }
        return userDao.updateUser(id, user);
    }
}
