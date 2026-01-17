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

    public User getUserByUserId(int id){
        return userDao.getUserByUserId(id);
    }
    
}
