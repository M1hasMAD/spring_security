package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtResponseDto { //отдаем токен юзеру на основе его логина и пароля
    private String token;
}
