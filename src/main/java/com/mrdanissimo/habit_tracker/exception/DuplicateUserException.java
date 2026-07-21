package com.mrdanissimo.habit_tracker.exception;

public class DuplicateUserException extends RuntimeException {
    public DuplicateUserException(String username) {
        super("Пользователь с таким именем уже существует: " + username);
    }
}