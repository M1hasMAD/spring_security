package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticationResponse { //выкидываем юзеру токен после регистрации или аутентификации юзера
    private String token;
}
