package dev.alexissdev.crudapp.security.filter;

import dev.alexissdev.crudapp.security.response.SecurityTokenResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static dev.alexissdev.crudapp.mapper.ObjectMapperFactory.MAPPER;
import static dev.alexissdev.crudapp.security.token.SecurityTokenConfiguration.HEADER_STRING;
import static dev.alexissdev.crudapp.security.token.SecurityTokenConfiguration.SECRET_KEY;
import static dev.alexissdev.crudapp.security.token.SecurityTokenConfiguration.TOKEN_PREFIX;
import static dev.alexissdev.crudapp.security.response.SecurityHttpResponse.sendUnauthorized;

public class SecurityValidationTokenFilter
        extends BasicAuthenticationFilter {

    public SecurityValidationTokenFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader(HEADER_STRING);
        if (header == null || header.isBlank() || !header.startsWith(TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        try {
            String token = header.substring(TOKEN_PREFIX.length());
            Claims claims = Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token).getPayload();
            String username = claims.getSubject();

            List<?> rolesObject = claims.get("roles", List.class);
            List<String> roles = rolesObject.stream()
                    .filter(obj -> obj instanceof String)
                    .map(obj -> (String) obj)
                    .toList();

            Collection<GrantedAuthority> authorities = roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(username, null, authorities);

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            chain.doFilter(request, response);
        } catch (Exception e) {
            SecurityTokenResponse tokenResponse = new SecurityTokenResponse(e.getMessage(), "Invalid token!");

            sendUnauthorized(response, MAPPER.writeValueAsBytes(tokenResponse));
        }
    }
}
