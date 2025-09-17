package com.example.carins.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();

    @Test
    void handleNotFoundException_shouldReturnNotFoundStatus() {
        NotFoundException exception = new NotFoundException("Not found");

        ResponseEntity<Map<String, String>> response =
                exceptionHandler.handleNotFoundException(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Not found", response.getBody().get("message"));
    }

    @Test
    void handleRequestValidationException_shouldReturnBadRequestStatus() {
        RequestValidationException exception = new RequestValidationException("Bad request");

        ResponseEntity<Map<String, String>> response =
                exceptionHandler.handleRequestValidationException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Bad request", response.getBody().get("message"));
    }

    @Test
    void handleResponseStatusException_shouldReturnCorrectStatus() {
        ResponseStatusException exception =
                new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid input");

        ResponseEntity<Map<String, String>> response =
                exceptionHandler.handleResponseStatusException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Invalid input", response.getBody().get("message"));
    }
}