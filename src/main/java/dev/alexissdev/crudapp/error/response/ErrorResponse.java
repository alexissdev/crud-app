package dev.alexissdev.crudapp.error.response;

public record ErrorResponse(String field, String rejectedValue, String message) { }
