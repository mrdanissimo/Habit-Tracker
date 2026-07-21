package com.mrdanissimo.habit_tracker.exception;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException() {
        super("Неверный логин или пароль");
    }
}