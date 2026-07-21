package com.mrdanissimo.habit_tracker.exception;

public class AlreadyCompletedException extends RuntimeException {
    public AlreadyCompletedException() {
        super("Привычка уже отмечена за сегодня.");
    }
}