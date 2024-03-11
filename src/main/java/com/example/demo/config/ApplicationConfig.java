package com.example.demo.config;

import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {
    private final UserRepository userRepository;

    @Bean
    public UserDetailsService userDetailsService() { //получаем юзера из бд для проверки есть ли он вообще в бд
        return username -> userRepository.findByEmail(username) // //имя юзера = почта юзера (спринг понимает только имя юзера, но по факту ищем и передаем почту)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception { // интерфейс для управления множества Provider'ов
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() { // механизм проверки(аутентификации)
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(); // проверяем (в бд):
        provider.setUserDetailsService(userDetailsService()); // имя юзера
        provider.setPasswordEncoder(passwordEncoder()); // и пароль
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() { // шифрования паролей
        return new BCryptPasswordEncoder(); // при помощи хеширования
    }
}
