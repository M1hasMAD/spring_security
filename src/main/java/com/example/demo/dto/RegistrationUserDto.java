package com.example.demo.dto;

import lombok.Data;

@Data
public class RegistrationUserDto { // поля необходимые для регистрации юзера
    private String username;
    private String password;
    private String confirmPassword;
    private String email;
}
