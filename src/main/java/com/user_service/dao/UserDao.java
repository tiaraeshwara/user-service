package com.user_service.dao;

import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.util.CollectionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.dao.DataAccessException;

import com.user_service.dao.mappers.UserMapper;
import com.user_service.model.User;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Primary
@Repository("userDao")
public class UserDao {

    @Autowired
    @Qualifier("userJdbcTemplate")
    JdbcTemplate jdbcTemplate;

    public String getGreetingDao() {
        return "Hello, user Dao!";
    }

    public List<User> getUserInformation() {
        try {
            String query = "SELECT FirstName, LastName, Age, Gender, City, PhoneNumber, Email FROM ts.user_info";

            NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
            MapSqlParameterSource parameters = new MapSqlParameterSource();
            List<User> users = template.query(query, parameters, new UserMapper());
            if (!CollectionUtils.isEmpty(users)) {
                return users;
            }
            return null;
        } catch (DataAccessException e) {
            String errorMsg = "Error occurred while fetching user information from the database.";
            throw new RuntimeException(errorMsg, e);
        }
    }

    public User getUserByUserId(int id) {
        try {
            String query = "SELECT FirstName, LastName, Age, Gender, City, PhoneNumber, Email FROM ts.user_info WHERE UserID = :id";

            NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
            MapSqlParameterSource parameters = new MapSqlParameterSource();
            parameters.addValue("id", id);
            List<User> users = template.query(query, parameters, new UserMapper());
            if (!CollectionUtils.isEmpty(users)) {
                return users.get(0);
            }
            return null;
        } catch (DataAccessException e) {
            String errorMsg = "Error fetching user information by userId.";
            throw new RuntimeException(errorMsg, e);
        }
    }
}