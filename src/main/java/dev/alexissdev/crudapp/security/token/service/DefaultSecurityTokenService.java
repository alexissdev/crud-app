package dev.alexissdev.crudapp.security.token.service;

import dev.alexissdev.crudapp.redis.RedisConfiguration;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static dev.alexissdev.crudapp.security.token.configuration.SecurityTokenConfiguration.*;

@Service
public class DefaultSecurityTokenService
        implements SecurityTokenService {

    private final RedisTemplate<String, Object> redis;

    @Autowired
    public DefaultSecurityTokenService(RedisTemplate<String, Object> redis) {
        this.redis = redis;
    }

    @Override
    public String generateAccessToken(UserDetails user) {
        return Jwts.builder()
                .subject(user.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .claim("roles", user.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList())
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public String generateRefreshToken(UserDetails user) {
        String refreshToken = UUID.randomUUID().toString();
        String key = String.format(RedisConfiguration.REFRESH_TOKEN_KEY, user.getUsername());

        redis.opsForValue().set(
                key,
                refreshToken,
                REFRESH_EXPIRATION,
                TimeUnit.MILLISECONDS
        );

        return refreshToken;
    }

    @Override
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    @Override
    public boolean isTokenValid(String token, UserDetails user) {
        return extractUsername(token).equals(user.getUsername()) && !isExpired(token);
    }

    @Override
    public boolean isRefreshTokenValid(String username, String refreshToken) {
        String key = String.format(RedisConfiguration.REFRESH_TOKEN_KEY, username);
        String storedToken = (String) redis.opsForValue().get(key);

        return storedToken != null && storedToken.equals(refreshToken);
    }

    @Override
    public String rotateRefreshToken(String username) {
        redis.delete(String.format(RedisConfiguration.REFRESH_TOKEN_KEY, username));

        return generateRefreshToken(
                new org.springframework.security.core.userdetails.User(
                        username, "", List.of()
                )
        );
    }

    @Override
    public void logout(String username, String accessToken) {
        redis.delete(String.format(RedisConfiguration.REFRESH_TOKEN_KEY, username));

        redis.opsForValue().set(
                "bl:" + accessToken,
                "revoked",
                EXPIRATION_TIME,
                TimeUnit.MILLISECONDS
        );
    }

    private boolean isExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}
