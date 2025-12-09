package dev.alexissdev.crudapp.security.filter;

import dev.alexissdev.crudapp.security.response.SecurityAuthResponse;
import dev.alexissdev.crudapp.security.response.SecurityLoginResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Date;

import static dev.alexissdev.crudapp.mapper.ObjectMapperFactory.MAPPER;
import static dev.alexissdev.crudapp.security.token.SecurityTokenConfiguration.AUTH_ERRORS;
import static dev.alexissdev.crudapp.security.token.SecurityTokenConfiguration.EXPIRATION_TIME;
import static dev.alexissdev.crudapp.security.token.SecurityTokenConfiguration.HEADER_STRING;
import static dev.alexissdev.crudapp.security.token.SecurityTokenConfiguration.SECRET_KEY;
import static dev.alexissdev.crudapp.security.token.SecurityTokenConfiguration.TOKEN_PREFIX;
import static dev.alexissdev.crudapp.security.response.SecurityHttpResponse.sendAuthorization;
import static dev.alexissdev.crudapp.security.response.SecurityHttpResponse.sendUnauthorized;

public class SecurityAuthenticationFilter
        extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public SecurityAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            SecurityLoginResponse loginResponse = MAPPER.readValue(request.getInputStream(), SecurityLoginResponse.class);
            if (loginResponse.username() == null || loginResponse.password() == null) {
                throw new AuthenticationServiceException("Username or password is missing");
            }

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(loginResponse.username(), loginResponse.password());
            return authenticationManager.authenticate(authToken);
        } catch (IOException e) {
            throw new AuthenticationServiceException("Error reading authentication request body", e);
        }
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException {
        Object possibleUser = authResult.getPrincipal();
        if (!(possibleUser instanceof org.springframework.security.core.userdetails.User user)) {
            throw new AuthenticationServiceException("Authentication failed");
        }

        String jwt = Jwts.builder()
                .subject(user.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .claim("roles", user.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList())
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();

        response.addHeader(HEADER_STRING, String.format("%s%s", TOKEN_PREFIX, jwt));

        SecurityAuthResponse authResponse = new SecurityAuthResponse(user.getUsername(),
                String.format("User %s has logged in successfully", user.getUsername()), jwt);

        sendAuthorization(response, MAPPER.writeValueAsBytes(authResponse));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        String message = AUTH_ERRORS.getOrDefault(failed.getClass(), "Authentication failed");
        logger.warn("Authentication error: {}", failed);
        SecurityAuthResponse authResponse = new SecurityAuthResponse(null, message, null);

        response.addHeader("WWW-Authenticate", "Bearer");
        sendUnauthorized(response, MAPPER.writeValueAsBytes(authResponse));
    }
}
