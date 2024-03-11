package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticationRequest { // то что нужно для авторизации юзера
    private String email;
    private String password;
}
