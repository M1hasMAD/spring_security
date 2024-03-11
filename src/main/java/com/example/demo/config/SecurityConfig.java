package com.example.demo.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity // вкл поддержку безопасности веб-приложения
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    // создаем цепочку фильтров
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {//HttpSecurity-объект,дающий возможность настраивать безопасность для определенных http-запросов(по умолчанию прим. ко всем запросам)
        http
                .csrf(AbstractHttpConfigurer::disable) // выкл csrf-защиту ...
                .authorizeHttpRequests // настраиваем правила доступа к ресурсам ->
                        (auth -> auth.requestMatchers("/api/v1/auth/**") //-> доступ к этому ресурсу ->
                        .permitAll() //-> доступен без аутентификации
                        .anyRequest() // для всех остальных запросов ->
                        .authenticated()) //-> нужна аутентификация
                .sessionManagement // настраиваем управление сеансами
                        (session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //stateless - сеансы не будут создаваться и сост. сеанса не сохр. между сеансами
                .authenticationProvider(authenticationProvider) // добавляем аутентификацию юзера
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
