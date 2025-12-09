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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
        String username = tokenService.extractUsername(request.refreshToken());
        User user = userService.findByUsername(username).orElseThrow();

        return new TokenResponse(tokenService.generateAccessToken(UserDetailsFactory.create(user)));
    }

}
