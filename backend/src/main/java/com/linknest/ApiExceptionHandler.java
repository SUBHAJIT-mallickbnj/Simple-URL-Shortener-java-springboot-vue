package com.linknest;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;

class UrlNotFoundException extends RuntimeException {
    UrlNotFoundException(String message) { super(message); }
}

class InvalidUrlException extends RuntimeException {
    InvalidUrlException(String message) { super(message); }
}

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(UrlNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> notFound(UrlNotFoundException exception) { return Map.of("error", exception.getMessage()); }

    @ExceptionHandler({InvalidUrlException.class, IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> badRequest(RuntimeException exception) { return Map.of("error", exception.getMessage()); }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> invalidRequest(MethodArgumentNotValidException exception) {
        return Map.of("error", "URL is required and must be 2048 characters or fewer");
    }
}