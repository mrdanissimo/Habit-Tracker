package com.mrdanissimo.habit_tracker.dto;

import lombok.Getter;
import java.time.LocalDate;

public class RecordResponse {
    @Getter
    private Long id;
    @Getter
    private Long habitId;
    @Getter
    private LocalDate date;

    public void setId(Long id) {this.id = id; }

    public void setHabitId(Long habitId) { this.habitId = habitId; }

    public void setDate(LocalDate date) { this.date = date; }
}
