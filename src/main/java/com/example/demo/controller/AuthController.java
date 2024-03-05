package com.example.demo.controller;

import com.example.demo.dto.JwtRequestDto;
import com.example.demo.dto.JwtResponseDto;
import com.example.demo.exception.AppError;
import com.example.demo.service.UserService;
import com.example.demo.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final UserService userService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager; // проверяет: логин и пароль есть в бд -> юзер есть в бд(зареган на сайте) -> даем токем
                                                                // логина и пароля нет в бд -> юзера нет в бд(незареган на сайте) -> не делаем токен

    @PostMapping("/auth")
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequestDto jwtRequest) { // по логину и паролю отдаем токен юзеру
        log.info("Here");
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(), jwtRequest.getPassword()));
        } catch (BadCredentialsException e) { // плохие(неверные) логин и пароль
            return new ResponseEntity<>(new AppError(HttpStatus.UNAUTHORIZED.value(), "Wrong Login or Password"), HttpStatus.UNAUTHORIZED);
        }
        UserDetails userDetails = userService.loadUserByUsername(jwtRequest.getUsername()); // все ок(пароль сверился) -> нужно достать юзера из бд по имени из токена
        String token = jwtTokenUtils.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponseDto(token));
    }
}
