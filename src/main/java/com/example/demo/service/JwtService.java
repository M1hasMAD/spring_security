package com.example.demo.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${jwt.secret}")
    private String secret; // набор символов в конце токена установленный только в приложении

    public String extractUsernameFromToken(String token) { // достаем имя(почта) юзера из токена
        return extractClaims(token, Claims::getSubject); // getSubject() - геттер для имени из токена
    }

    // абстракция над получением всех данных, то есть получаем именно те данные которые нам нужны
    public <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    // достаем все-все данные из токена (распарсиваем токен и из него получаем объект Claims)
    private Claims getClaimsFromToken(String token) { //объект Claims - вся инфа об юзере полученая из токена
        return Jwts.parserBuilder() // создаем парсер
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJwt(token) // распарсиваем токен -> на выходе объект Jwt вместе с Claims
                .getBody(); // извлекаем Claims из Jwt-объекта
    }

    // метод для создания подписи в конце токена
    private Key getSignKey() { // объект Key нужен для подписи в конце jwt-токена
        byte[] keyBytes = Decoders.BASE64.decode(secret); // декодим секрет в массив байт ->
        return Keys.hmacShaKeyFor(keyBytes); // -> преобразует массив байт в объект Key
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) { //генерация токана для юзера
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername()) // по фатку это почта а не имя юзера, но для спринга это имя
                .setIssuedAt(new Date(System.currentTimeMillis())) // время создание токена
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24)) // как долго токен можно будет юзать (щас на 24 часа установленно)
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateToken(UserDetails userDetails) { // метод для вызова из класса авторизации
        return generateToken(new HashMap<>(), userDetails);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) { // валидация токена по имени юзера и сроку годности токена
        final String username = extractUsernameFromToken(token); // берем имя из токена
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token); // имя(по факту почта) юзера из токена совпадает почте в переданных данных юзером и токен не просрочен
    }

    private boolean isTokenExpired(String token) { // проверяем срок годности токена
        return extractExpiration(token).before(new Date()); // сравниваем срок годности с текущей датой
    }

    private Date extractExpiration(String token) { // извлекаем срок действия токена из токена
        return extractClaims(token, Claims::getExpiration); // getExpiration() - извлечение даты (срока годности)
    }
}
