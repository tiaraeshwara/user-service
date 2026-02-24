package com.user_service.dao;

import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Random;

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
            String query = "SELECT UserID, FirstName, LastName, Age, Gender, City, PhoneNumber, Email FROM ts.user_info";

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
            String query = "SELECT UserID, FirstName, LastName, Age, Gender, City, PhoneNumber, Email FROM ts.user_info WHERE UserID = :id";

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

    public User getUserByUserEmail(String email) {
        try {
            String query = "SELECT UserID, FirstName, LastName, Age, Gender, City, PhoneNumber, Email FROM ts.user_info WHERE Email = :email";

            NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
            MapSqlParameterSource parameters = new MapSqlParameterSource();
            parameters.addValue("email", email);
            List<User> users = template.query(query, parameters, new UserMapper());
            if (!CollectionUtils.isEmpty(users)) {
                return users.get(0);
            }
            return null;
        } catch (DataAccessException e) {
            String errorMsg = "Error fetching user by email.";
            throw new RuntimeException(errorMsg, e);
        }
    }

    public User addUser(User user) {
        try {
            // Basic DAO-level validation
            if (user == null) {
                throw new IllegalArgumentException("User must be provided");
            }
            if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
                throw new IllegalArgumentException("email must be provided");
            }
            // prevent duplicate email
            User existing = getUserByUserEmail(user.getEmail());
            if (existing != null) {
                throw new IllegalArgumentException("A user with the provided email already exists");
            }

            String query = "INSERT INTO ts.user_info (UserID, FirstName, LastName, Age, Gender, City, PhoneNumber, Email) VALUES (:userId, :firstName, :lastName, :age, :gender, :city, :phoneNumber, :email)";

            NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
            MapSqlParameterSource parameters = new MapSqlParameterSource();

            // generate a random userId and ensure it doesn't already exist (guard against rare collisions)
            int userId;
            int attempts = 0;
            do {
                userId = new Random().nextInt(Integer.MAX_VALUE - 1) + 1; // avoid zero
                String checkQuery = "SELECT UserID FROM ts.user_info WHERE UserID = :userId";
                MapSqlParameterSource checkParams = new MapSqlParameterSource();
                checkParams.addValue("userId", userId);
                List<Integer> ids = template.query(checkQuery, checkParams, (rs, rowNum) -> rs.getInt("UserID"));
                if (ids == null || ids.isEmpty()) {
                    break;
                }
                attempts++;
            } while (attempts < 5);

            parameters.addValue("userId", userId);
            parameters.addValue("firstName", user.getFirstName());
            parameters.addValue("lastName", user.getLastName());
            parameters.addValue("age", user.getAge());
            parameters.addValue("gender", user.getGender());
            parameters.addValue("city", user.getCity());
            parameters.addValue("phoneNumber", user.getPhoneNumber());
            parameters.addValue("email", user.getEmail());
            template.update(query, parameters);
            return user;
        } catch (DataAccessException e) {
            String errorMsg = "Error inserting user information into the database.";
            throw new RuntimeException(errorMsg, e);
        } 
    }

    /**
     * DAO-level login: attempt to find a user by email and phoneNumber.
     * Returns the matched User or null if not found.
     */
    public User login(String email, String phoneNumber) {
        try {
            if (email == null || email.trim().isEmpty()) {
                throw new IllegalArgumentException("email must be provided");
            }
            String query = "SELECT FirstName, LastName, Age, Gender, City, PhoneNumber, Email FROM ts.user_info WHERE Email = :email AND PhoneNumber = :phoneNumber";
            NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
            MapSqlParameterSource parameters = new MapSqlParameterSource();
            parameters.addValue("email", email);
            parameters.addValue("phoneNumber", phoneNumber);
            List<User> users = template.query(query, parameters, new UserMapper());
            if (!CollectionUtils.isEmpty(users)) {
                return users.get(0);
            }
            return null;
        } catch (DataAccessException e) {
            String errorMsg = "Error executing login query in the database.";
            throw new RuntimeException(errorMsg, e);
        }
    }

    public User updateUser(int id, User user) {
        try {
            String query = "UPDATE ts.user_info SET FirstName = :firstName, LastName = :lastName, Age = :age, Gender = :gender, City = :city, PhoneNumber = :phoneNumber, Email = :email WHERE UserID = :userId";

            NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
            MapSqlParameterSource parameters = new MapSqlParameterSource();
            parameters.addValue("userId", id);
            parameters.addValue("firstName", user.getFirstName());
            parameters.addValue("lastName", user.getLastName());
            parameters.addValue("age", user.getAge());
            parameters.addValue("gender", user.getGender());
            parameters.addValue("city", user.getCity());
            parameters.addValue("phoneNumber", user.getPhoneNumber());
            parameters.addValue("email", user.getEmail());
            template.update(query, parameters);
            return user;
        } catch (DataAccessException e) {
            String errorMsg = "Error inserting user information into the database.";
            throw new RuntimeException(errorMsg, e);
        }
    }

    public boolean deleteUser(int id) {
        try {
            String query = "DELETE FROM ts.user_info WHERE UserID = :userId";
            NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
            MapSqlParameterSource parameters = new MapSqlParameterSource();
            parameters.addValue("userId", id);
            int rows = template.update(query, parameters);
            return rows > 0;
        } catch (DataAccessException e) {
            String errorMsg = "Error deleting user information from the database.";
            throw new RuntimeException(errorMsg, e);
        }
    }

}