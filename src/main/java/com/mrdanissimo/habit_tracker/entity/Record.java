package com.mrdanissimo.habit_tracker.entity;

import lombok.Data;
import java.time.LocalDate;

@Data
public class Record {
    private Long id; // номер записи
    private Long habitId; // привязка к привычке
    private LocalDate date; // дата выполнения

}
