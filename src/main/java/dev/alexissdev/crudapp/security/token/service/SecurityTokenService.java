package dev.alexissdev.crudapp.security.token.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface SecurityTokenService {

    /**
     * Generates an access token for the provided user.
     *
     * @param user the user details for whom the access token will be generated
     * @return a newly generated access token as a string
     */

    String generateAccessToken(UserDetails user);

    /**
     * Generates a refresh token for the provided user.
     *
     * @param user the user details for whom the refresh token will be generated
     * @return a newly generated refresh token as a string
     */

    String generateRefreshToken(UserDetails user);

    /**
     * Extracts the username (subject) from the given token.
     *
     * @param token the token from which the username will be extracted
     * @return the username contained in the token
     */

    String extractUsername(String token);

    /**
     * Validates the given token by checking its username and verifying that it has not expired.
     *
     * @param token the token to be validated
     * @param user the user details to compare against the token's username
     * @return true if the token's username matches the provided user and the token is not expired, false otherwise
     */

    boolean isTokenValid(String token, UserDetails user);
}
