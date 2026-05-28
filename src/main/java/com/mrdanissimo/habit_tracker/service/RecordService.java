package com.mrdanissimo.habit_tracker.service;

import com.mrdanissimo.habit_tracker.dto.RecordResponse;
import com.mrdanissimo.habit_tracker.entity.Record;
import com.mrdanissimo.habit_tracker.exception.HabitNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class RecordService {

    private final Map<Long, List<Record>> recordRepository = new HashMap<>();
    private Long currentId = 1L;

    private final HabitService habitService;

    public RecordService(HabitService habitService) {
        this.habitService = habitService;
    }

    // Отметка выполнения привычки за текущиц день
    public RecordResponse markCompleted(Long habitId) {

        // Проверяем, что привычка существует
        habitService.getHabitEntity(habitId);

        // Создаем новую запись
        Record record = new Record();
        record.setId(currentId++);
        record.setHabitId(habitId);
        record.setDate(LocalDate.now());

        // Добавляем в Map
        recordRepository.computeIfAbsent(habitId, k -> new ArrayList<>()).add(record);

        return mapToResponse(record);
    }

    // Получение всех записей выполнения привычки
    public List<RecordResponse> getAllByHabitId(Long habitId) {
        if (!habitService.existsById(habitId)) {
            throw new HabitNotFoundException(habitId);
        }
        return recordRepository.getOrDefault(habitId, Collections.emptyList()).stream().map(this::mapToResponse).toList();
    }

    // Преобразование entity в dto
    private RecordResponse mapToResponse(Record record) {
        RecordResponse res = new RecordResponse();
        res.setId(record.getId());
        res.setHabitId(record.getHabitId());
        res.setDate(record.getDate());
        return res;
    }
}