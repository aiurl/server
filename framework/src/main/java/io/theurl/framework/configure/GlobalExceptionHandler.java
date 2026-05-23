package io.theurl.framework.configure;

import com.neroyun.mediator.internal.AggregateException;
import com.neroyun.mediator.validation.ValidationException;
import io.theurl.framework.security.AccountException;
import io.theurl.framework.security.CredentialException;
import io.theurl.framework.security.UnauthorizedAccessException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.CompletionException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final String name = "message";
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(AccountException.class)
    public ResponseEntity<Map<String, String>> handleAccountException(AccountException exception) {
        LOGGER.error(exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                             .body(Map.of(name, exception.getLocalizedMessage()));
    }

    @ExceptionHandler(CredentialException.class)
    public ResponseEntity<Map<String, String>> handleCredentialException(CredentialException exception) {
        LOGGER.error(exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                             .body(Map.of(name, exception.getLocalizedMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException exception) {
        LOGGER.error(exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(Map.of(name, exception.getLocalizedMessage()));
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<Map<String, String>> handleUnauthorizedAccessException(UnauthorizedAccessException exception) {
        LOGGER.error(exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                             .body(Map.of(name, exception.getLocalizedMessage()));
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(ValidationException exception) {
        LOGGER.error(exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(Map.of(name, exception.getLocalizedMessage()));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleEntityNotFoundException(EntityNotFoundException exception) {
        LOGGER.error(exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body(Map.of(name, exception.getLocalizedMessage()));
    }

    @ExceptionHandler(AggregateException.class)
    public ResponseEntity<Map<String, String>> handleAggregateException(AggregateException exception) {
        LOGGER.error(exception.getMessage(), exception);
        Throwable cause = exception.getExceptions().getFirst();
        LOGGER.debug("AggregateException contains {} exceptions, handling the first one: {}", exception.getExceptions().size(), cause.getMessage());
        return handleGeneralException(cause);
    }

    @ExceptionHandler(CompletionException.class)
    public ResponseEntity<Map<String, String>> handleCompletionException(CompletionException exception) {
        LOGGER.error(exception.getMessage(), exception);
        Throwable cause = exception.getCause();
        return handleGeneralException(cause);
    }

    private ResponseEntity<Map<String, String>> handleGeneralException(Throwable exception) {
        if (exception == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return switch (exception) {
            case AccountException accountException -> handleAccountException(accountException);
            case EntityNotFoundException entityNotFoundException ->
                handleEntityNotFoundException(entityNotFoundException);
            case CredentialException credentialException -> handleCredentialException(credentialException);
            case IllegalArgumentException illegalArgumentException ->
                handleIllegalArgumentException(illegalArgumentException);
            case UnauthorizedAccessException unauthorizedAccessException ->
                handleUnauthorizedAccessException(unauthorizedAccessException);
            case ValidationException validationException -> handleValidationException(validationException);
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                     .body(Map.of(name, exception.getLocalizedMessage()));
        };
    }
}
