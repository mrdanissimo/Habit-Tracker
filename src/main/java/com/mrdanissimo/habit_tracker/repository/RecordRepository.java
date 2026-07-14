package com.mrdanissimo.habit_tracker.repository;

import com.mrdanissimo.habit_tracker.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {
    @Query("SELECT r FROM Record r JOIN FETCH r.habit WHERE r.habit.id = :habitId")
    List<Record> findAllByHabitId(@Param("habitId") Long habitId);

    boolean existsByHabitIdAndDate(Long habitId, LocalDate date);

    int countByDate(LocalDate date);

    @Query("SELECT COUNT(r) FROM Record r JOIN r.habit h WHERE r.date = :date AND h.user.id = :userId")
    int countByDateAndUserId(@Param("date") LocalDate date, @Param("userId") Long userId);
}