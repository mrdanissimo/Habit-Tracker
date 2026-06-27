package com.mrdanissimo.habit_tracker.repository;

import com.mrdanissimo.habit_tracker.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {
    List<Record> findAllByHabitId(Long habitId);

    boolean existsByHabitIdAndDate(Long habitId, LocalDate date);

    int countByDate(LocalDate date);
}
