package com.prueba.microservice1.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Manejador global de excepciones.
 * Centraliza el manejo de errores y retorna respuestas consistentes.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Estructura de respuesta de error.
     */
    public record ErrorResponse(
            int status,
            String mensaje,
            String detalle,
            LocalDateTime timestamp
    ) {
        public static ErrorResponse of(HttpStatus status, String mensaje, String detalle) {
            return new ErrorResponse(status.value(), mensaje, detalle, LocalDateTime.now());
        }

        public static ErrorResponse of(HttpStatus status, String mensaje) {
            return new ErrorResponse(status.value(), mensaje, null, LocalDateTime.now());
        }
    }

    /**
     * Maneja ClienteNotFoundException.
     */
    @ExceptionHandler(ClienteNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleClienteNotFound(ClienteNotFoundException ex) {
        log.warn("Cliente no encontrado: {}", ex.getMessage());
        ErrorResponse error = ErrorResponse.of(HttpStatus.NOT_FOUND, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /**
     * Maneja errores de validación.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        log.warn("Error de validación: {}", ex.getMessage());

        Map<String, String> errores = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fieldError -> fieldError.getDefaultMessage() != null
                                ? fieldError.getDefaultMessage()
                                : "Error de validación",
                        (existing, replacement) -> existing // En caso de duplicados, mantener el primero
                ));

        Map<String, Object> response = Map.of(
                "status", HttpStatus.BAD_REQUEST.value(),
                "mensaje", "Error de validación",
                "errores", errores,
                "timestamp", LocalDateTime.now()
        );

        return ResponseEntity.badRequest().body(response);
    }

    /**
     * Maneja excepciones genéricas.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        log.error("Error interno del servidor: ", ex);
        ErrorResponse error = ErrorResponse.of(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Error interno del servidor",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    /**
     * Maneja IllegalArgumentException.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        log.warn("Argumento inválido: {}", ex.getMessage());
        ErrorResponse error = ErrorResponse.of(HttpStatus.BAD_REQUEST, ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }
}
