package dev.alexissdev.crudapp.auth;

import dev.alexissdev.crudapp.security.token.request.RefreshRequest;
import dev.alexissdev.crudapp.security.token.response.TokenResponse;
import dev.alexissdev.crudapp.security.token.service.SecurityTokenService;
import dev.alexissdev.crudapp.user.User;
import dev.alexissdev.crudapp.user.service.UserService;
import dev.alexissdev.crudapp.util.UserDetailsFactory;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin()
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final SecurityTokenService tokenService;

    @Autowired
    public AuthController(UserService userService, SecurityTokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody User user) {
        if (user.isAdmin()) {
            user.setAdmin(false);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(user));
    }

    @PostMapping("/refresh")
    public TokenResponse refresh(@RequestBody RefreshRequest request) {
        String refreshToken = request.refreshToken();
        String username = tokenService.extractUsername(refreshToken);
        if (!tokenService.isRefreshTokenValid(username, refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }

        User user = userService.findByUsername(username).orElseThrow();
        UserDetails userDetails = UserDetailsFactory.create(user);

        String newAccessToken = tokenService.generateAccessToken(userDetails);
        String newRefreshToken = tokenService.rotateRefreshToken(username);

        return new TokenResponse(
                username,
                newAccessToken,
                newRefreshToken
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().build();
        }

        String accessToken = authHeader.substring(7);
        String username = tokenService.extractUsername(accessToken);
        tokenService.logout(username, accessToken);

        return ResponseEntity.noContent().build();
    }


}
