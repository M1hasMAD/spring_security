package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterRequest { // то что нужно ввести для регистрации
    private String firstname;
    private String lastname;
    private String email;
    private String password;
}
