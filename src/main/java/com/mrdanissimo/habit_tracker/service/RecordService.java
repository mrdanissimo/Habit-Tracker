package com.mrdanissimo.habit_tracker.service;

import com.mrdanissimo.habit_tracker.dto.RecordResponse;
import com.mrdanissimo.habit_tracker.entity.Habit;
import com.mrdanissimo.habit_tracker.entity.Record;
import com.mrdanissimo.habit_tracker.mapper.RecordMapper;
import com.mrdanissimo.habit_tracker.repository.RecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class RecordService {
    private final RecordRepository recordRepository;
    private final HabitService habitService;
    private final RecordMapper recordMapper;

    // Отметка выполнения привычки за текущий день
    @Transactional
    public RecordResponse markCompleted(Long habitId) {
        Habit habit = habitService.getHabitEntity(habitId);

        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!Objects.equals(habit.getUser().getUsername(), currentUsername)) {
            throw new RuntimeException("Доступ запрещен! Вы не можете отмечать чужие привычки.");
        }

        if (recordRepository.existsByHabitIdAndDate(habitId, LocalDate.now())) {
            throw new RuntimeException("Привычка уже отмечена за сегодня.");
        }

        Record record = new Record();
        record.setHabit(habit);
        record.setDate(LocalDate.now());

        Record saveRecord = recordRepository.save(record);
        return recordMapper.toResponse(saveRecord);
    }

    // Получение всех записей выполнения привычки
    @Transactional(readOnly = true)
    public List<RecordResponse> getAllByHabitId(Long habitId) {
        Habit habit = habitService.getHabitEntity(habitId);

        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!Objects.equals(habit.getUser().getUsername(), currentUsername)) {
            throw new RuntimeException("Доступ запрещен! Вы не можете просматривать историю чужих привычек.");
        }

        return recordRepository.findAllByHabitId(habitId).stream()
                .map(recordMapper::toResponse).toList();
    }
}