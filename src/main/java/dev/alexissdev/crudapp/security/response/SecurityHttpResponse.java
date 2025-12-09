package dev.alexissdev.crudapp.security.response;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static dev.alexissdev.crudapp.security.token.configuration.SecurityTokenConfiguration.*;

public class SecurityHttpResponse {

    /**
     * Sends an HTTP 200 OK response with the specified body content.
     *
     * @param response the HttpServletResponse object used to send the response
     * @param body     the response body as a byte array
     * @throws IOException if an I/O error occurs while writing the response
     */

    public static void sendAuthorization(HttpServletResponse response, byte[] body) throws IOException {
        sendJsonResponse(response, HttpServletResponse.SC_OK, body);
    }

    /**
     * Sends an HTTP 401 Unauthorized response with the specified body content.
     *
     * @param response the HttpServletResponse object used to send the response
     * @param body     the response body as a byte array
     * @throws IOException if an I/O error occurs while writing the response
     */
    public static void sendUnauthorized(HttpServletResponse response, byte[] body) throws IOException {
        sendJsonResponse(response, HttpServletResponse.SC_UNAUTHORIZED, body);
    }

    /**
     * Sends an HTTP JSON response with the specified status code and body content.
     *
     * @param response the HttpServletResponse object used to send the response
     * @param status   the HTTP status code to set for the response
     * @param body     the response body as a byte array
     * @throws IOException if an I/O error occurs while writing the response
     */

    public static void sendJsonResponse(HttpServletResponse response, int status, byte[] body) throws IOException {
        response.setStatus(status);
        response.setContentType(CONTENT_TYPE);
        response.setCharacterEncoding(ENCODING);
        response.getOutputStream().write(body);
        response.flushBuffer();
    }

}
