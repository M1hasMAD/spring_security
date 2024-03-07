package com.example.demo.dto;

import lombok.Data;

@Data
public class JwtRequestDto { // данные юзера для формирования токена
    private String username;
    private String password;
}
