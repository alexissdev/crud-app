package dev.alexissdev.crudapp.security.token;

import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;

import javax.crypto.SecretKey;
import java.util.Map;

public class SecurityTokenConfiguration {

    public static final long EXPIRATION_TIME = 3600_000;
    public static final SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build();
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String CONTENT_TYPE = "application/json";
    public static final String ENCODING = "UTF-8";
    public static final Map<Class<? extends AuthenticationException>, String> AUTH_ERRORS = Map.of(
            BadCredentialsException.class, "Invalid username or password",
            LockedException.class, "Account is locked",
            DisabledException.class, "Account is disabled",
            CredentialsExpiredException.class, "Credentials have expired"
    );

}
