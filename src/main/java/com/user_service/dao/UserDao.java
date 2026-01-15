package com.user_service.dao;

import org.springframework.stereotype.Repository;

@Repository("userDao")
public class UserDao {

    public UserDao() {
    }

    public String getGreetingDao() {
        return "Hello, user Dao!";
    }

}