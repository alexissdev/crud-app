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

    /**
     * Validates a refresh token stored in Redis for the given username.
     *
     * @param username the username associated with the refresh token
     * @param refreshToken the refresh token to be validated
     * @return true if the refresh token exists in Redis and matches the stored value
     */
    boolean isRefreshTokenValid(String username, String refreshToken);

    /**
     * Revokes the current refresh token associated with the given username and generates a new one.
     * The existing refresh token is removed from the Redis store, and a new token is created,
     * stored in its place, and returned to the caller.
     *
     * @param username the username for which the refresh token will be rotated
     * @return a newly generated refresh token as a string
     */

    String rotateRefreshToken(String username);

    /**
     * Logs out a user by:
     * 1. Deleting the refresh token from Redis
     * 2. Blacklisting the access token in Redis
     *
     * @param username the username of the user logging out
     * @param accessToken the access token to be revoked
     */
    void logout(String username, String accessToken);
}
