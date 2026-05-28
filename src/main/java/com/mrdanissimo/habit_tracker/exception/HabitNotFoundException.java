package com.mrdanissimo.habit_tracker.exception;

public class HabitNotFoundException extends RuntimeException{
    public HabitNotFoundException(Long id) {
        super("Привычка с ID" + id + " не найдена");
    }
}
