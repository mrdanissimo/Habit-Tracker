package com.mrdanissimo.habit_tracker.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data // аннотация для создания геттеров и сеттеров
public class Habit {
    private Long id; // уникальный номер
    private String name; // название привычки
    private String description; // описание привычки
    private int target; // цель привычки
    private LocalDateTime createdAt; // время создания
}
