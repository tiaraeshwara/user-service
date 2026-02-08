package com.user_service.dao.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;

import com.user_service.model.User;

public class UserMapper implements RowMapper<User> {

    @SuppressWarnings("null")
    @Override
    @Nullable
    public User mapRow(ResultSet rs, int rowNumber) throws SQLException {
        User user = new User();
        user.setFirstName(rs.getString("firstname"));
        user.setLastName(rs.getString("lastname"));
        user.setEmail(rs.getString("email"));
        user.setAge(rs.getInt("age"));
        user.setCity(rs.getString("city"));
        user.setPhoneNumber(rs.getString("phonenumber"));
        user.setGender(rs.getString("gender"));

        return user;
    }

}
