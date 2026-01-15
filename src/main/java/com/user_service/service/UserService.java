package com.user_service.service;

import org.springframework.stereotype.Service;

import com.user_service.dao.UserDao;

@Service("UserService")
public class UserService {
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public String getGreeting() {
        return userDao.getGreetingDao();
    }
    
}
