package com.mrdanissimo.habit_tracker.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "records")
public class Record {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // номер записи

    @ManyToOne(fetch = FetchType.LAZY) // каждая запись принадлежит одной привычке
    @JoinColumn(name = "habit_id", nullable = false) // колонка в таблице records
    private Habit habit; // привязка к привычке

    @Column(nullable = false)
    private LocalDate date; // дата выполнения
}
