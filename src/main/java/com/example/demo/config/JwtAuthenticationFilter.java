package com.example.demo.config;

import com.example.demo.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// класс перехватывающий любой http-запрос в наше приложение
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization"); // получаем заголовок из http-запроса
        final String jwt;
        final String email;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) { // если заголовок НЕ начинаемся на "Bearer ", то запрос идет дальше без аутентификации
            filterChain.doFilter(request, response);
            return;
        } // если начинается, то ->
        jwt = authHeader.substring(7); // -> извлекается префикс из заголовка
        email = jwtService.extractUsernameFromToken(jwt); // получаем имя(по факту получаем email) юзера из токена
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) { //если получили почту из токена и юзер еще не аутенцифицировался (не ввел логин и пароль), то -> пробуем аутенцифировать юзера
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(email); // получаем все данные о юзере по email'у
            if (jwtService.isTokenValid(jwt, userDetails)) { // если почта в запросе и в бд совпадают и токен не просрочен, то ->
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()); // создаем объект аутентифицированного юзера с его инфой и его ролях
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); // устанавливаем доп детали из http-запроса
                SecurityContextHolder.getContext().setAuthentication(authToken); // кладем созданный объект в контекст безопасности(типо сессия, установка аутентифицированного пользователя в текущем потоке выполнения)
            }
        }
        filterChain.doFilter(request, response); // кидаем запрос и ответ через цепочку фильтров
    }
}
