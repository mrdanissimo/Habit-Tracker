package com.mrdanissimo.habit_tracker.service;

import com.mrdanissimo.habit_tracker.entity.Record;
import com.mrdanissimo.habit_tracker.dto.StatsResponse;
import com.mrdanissimo.habit_tracker.entity.Habit;
import com.mrdanissimo.habit_tracker.repository.RecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class StatsServiceTest {
    private RecordRepository recordRepository;
    private HabitService habitService;
    private StatsService statsService;

    @BeforeEach
    void setUp() {
        recordRepository = Mockito.mock(RecordRepository.class);
        habitService = Mockito.mock(HabitService.class);
        statsService = new StatsService(recordRepository, habitService);
    }

    @Test
    void testCurrentStreak_withConsecutiveDays() {
        // 1. Готовим данные: привычка создана 3 дня назад
        Habit habit = new Habit();
        habit.setId(1L);
        habit.setCreatedAt(LocalDateTime.now().minusDays(3));

        List<Record> records = new ArrayList<>();
        records.add(createRecord(LocalDate.now()));            // Выполнено сегодня
        records.add(createRecord(LocalDate.now().minusDays(1))); // Выполнено вчера
        records.add(createRecord(LocalDate.now().minusDays(2))); // Выполнено позавчера

        when(habitService.getHabitEntity(1L)).thenReturn(habit);
        when(recordRepository.findAllByHabitId(1L)).thenReturn(records);

        StatsResponse stats = statsService.getStats(1L);

        assertEquals(3, stats.getCurrentStreak());
    }

    @Test
    void testCurrentStreak_breakOnGap() {
        Habit habit = new Habit();
        habit.setId(1L);
        habit.setCreatedAt(LocalDateTime.now().minusDays(5));

        List<Record> records = new ArrayList<>();
        records.add(createRecord(LocalDate.now())); // Сегодня — выполнено
        records.add(createRecord(LocalDate.now().minusDays(2))); // Позавчера — выполнено

        when(habitService.getHabitEntity(1L)).thenReturn(habit);
        when(recordRepository.findAllByHabitId(1L)).thenReturn(records);

        StatsResponse stats = statsService.getStats(1L);

        assertEquals(1, stats.getCurrentStreak());
    }

    @Test
    void testBestStreak_findsMaximum() {
        Habit habit = new Habit();
        habit.setId(1L);
        habit.setCreatedAt(LocalDateTime.now().minusDays(10));

        List<Record> records = new ArrayList<>();
        // Первая цепочка (3 дня подряд)
        records.add(createRecord(LocalDate.now().minusDays(7)));
        records.add(createRecord(LocalDate.now().minusDays(6)));
        records.add(createRecord(LocalDate.now().minusDays(5)));

        // Вторая цепочка (2 дня подряд)
        records.add(createRecord(LocalDate.now().minusDays(1)));
        records.add(createRecord(LocalDate.now()));

        when(habitService.getHabitEntity(1L)).thenReturn(habit);
        when(recordRepository.findAllByHabitId(1L)).thenReturn(records);

        StatsResponse stats = statsService.getStats(1L);

        assertEquals(3, stats.getBestStreak());
    }

    @Test
    void testCompletionRate_isCorrect() {
        Habit habit = new Habit();
        habit.setId(1L);
        habit.setCreatedAt(LocalDateTime.now().minusDays(3));

        List<Record> records = new ArrayList<>();
        records.add(createRecord(LocalDate.now().minusDays(2)));
        records.add(createRecord(LocalDate.now())); // Всего 2 отметки из 4 возможных дней

        when(habitService.getHabitEntity(1L)).thenReturn(habit);
        when(recordRepository.findAllByHabitId(1L)).thenReturn(records);

        StatsResponse stats = statsService.getStats(1L);

        assertEquals(50.0, stats.getCompletionRate());
    }
    private Record createRecord(LocalDate date) {
        Record record = new Record();
        record.setDate(date);
        return record;
    }
}
