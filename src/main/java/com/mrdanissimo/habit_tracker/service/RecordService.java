package com.mrdanissimo.habit_tracker.service;

import com.mrdanissimo.habit_tracker.dto.RecordResponse;
import com.mrdanissimo.habit_tracker.entity.Habit;
import com.mrdanissimo.habit_tracker.entity.Record;
import com.mrdanissimo.habit_tracker.repository.RecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class RecordService {
    private final RecordRepository recordRepository;
    private final HabitService habitService;

    // Отметка выполнения привычки за текущиц день
    @Transactional
    public RecordResponse markCompleted(Long habitId) {
        // Проверка отметки привычки за день
        if (recordRepository.existsByHabitIdAndDate(habitId, LocalDate.now())) {
            throw new RuntimeException("Привычка уже отмечена за сегодня.");
        }
        Habit habit = habitService.getHabitEntity(habitId);
        Record record = new Record();
        record.setHabit(habit);
        record.setDate(LocalDate.now());

        Record saveRecord = recordRepository.save(record);
        return mapToResponce(saveRecord);
    }

    // Получение всех записей выполнения привычки
    @Transactional(readOnly = true)
    public List<RecordResponse> getAllByHabitId(Long habitId) {
        habitService.getHabitEntity(habitId); // Проверяем существование привычки
        return recordRepository.findAllByHabitId(habitId).stream().map(this::mapToResponce).toList();
    }

    private RecordResponse mapToResponce(Record record) {
        RecordResponse res = new RecordResponse();
        res.setHabitId(record.getHabit().getId());
        res.setId(record.getId());
        res.setDate(record.getDate());
        return res;
    }
}