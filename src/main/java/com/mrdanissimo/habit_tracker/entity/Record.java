package com.mrdanissimo.habit_tracker.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "habit")
public class Record {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // номер записи

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "habit_id", nullable = false)
    private Habit habit; // привязка к привычке

    @Column(nullable = false)
    private LocalDate date; // дата выполнения

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Record record = (Record) o;
        return id != null && id.equals(record.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}