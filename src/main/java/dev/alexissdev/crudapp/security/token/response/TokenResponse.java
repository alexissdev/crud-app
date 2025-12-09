package dev.alexissdev.crudapp.security.token.response;

public record TokenResponse(String username, String accessToken, String refreshToken) {
}
