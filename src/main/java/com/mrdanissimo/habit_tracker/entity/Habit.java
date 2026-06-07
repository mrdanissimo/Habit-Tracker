package com.mrdanissimo.habit_tracker.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data // аннотация для создания геттеров и сеттеров
@Entity
@Table(name = "habits")
public class Habit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // база сама генерирует Id
    private Long id; // уникальный номер

    @Column(nullable = false)
    private String name; // название привычки

    private String description; // описание привычки

    @Column(nullable = false)
    private int target; // цель привычки

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt; // время создания

    @OneToMany(mappedBy = "habit", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Record> record = new ArrayList<>(); // список записей, связанных с привычкой
}
