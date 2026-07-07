package com.mrdanissimo.habit_tracker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // Привычки нет
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleBadCredentials(RuntimeException ex) {
        // Если в сообщении речь идет о неверном пароле или логине
        if (ex.getMessage().contains("логин") || ex.getMessage().contains("пароль")) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED) // Честный 401 статус
                    .body(Map.of("error", ex.getMessage()));
        }

        // Для всех остальных RuntimeException отдаем 500
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Внутренняя ошибка сервера"));
    }
    // Обработка ошибок dto
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}
