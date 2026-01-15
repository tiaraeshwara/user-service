package com.user_service.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private String firstName;
    private String lastName;  
    private int age;
    private String gender;
    private String city;
    private String email;
    private String phoneNumber;
}
