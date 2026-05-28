package com.mrdanissimo.habit_tracker.service;

import com.mrdanissimo.habit_tracker.dto.HabitRequest;
import com.mrdanissimo.habit_tracker.dto.HabitResponse;
import com.mrdanissimo.habit_tracker.entity.Habit;
import com.mrdanissimo.habit_tracker.exception.HabitNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class HabitService {
    // Наша база данных
    private final Map<Long, Habit> habitRepository = new HashMap<>();
    private Long currentId = 1L;

    // Создание новой привычки
    public HabitResponse create(HabitRequest request) {
        Habit habit = new Habit();
        habit.setId(currentId);

        currentId++;
        // Перекладываем данные из request в объект Habit
        habit.setName(request.getName());
        habit.setDescription(request.getDescription());
        habit.setTarget(request.getTarget());
        habit.setCreatedAt(LocalDateTime.now());

        habitRepository.put(habit.getId(), habit);
        log.info("Создана привычка: {}", habit.getName());
        return mapToResponse(habit);
    }

    // Получение всех привычек
    public List<HabitResponse> getAll() {
        return habitRepository.values().stream().map(this::mapToResponse).toList();
    }

    // Получение привычки по ID
    public HabitResponse getById(Long id) {
        Habit habit = habitRepository.get(id);
        // Проверка на наличие привычки
        if (habit == null) {
            throw new HabitNotFoundException(id);
        }
        return mapToResponse(habit);
    }

    // Обновление привычки
    public HabitResponse update(Long id, HabitRequest request) {
        Habit habit = habitRepository.get(id);
        // Проверка на наличие привычки
        if (habit == null) {
            throw new HabitNotFoundException(id);
        }
        // Обновление данных
        habit.setName(request.getName());
        habit.setDescription(request.getDescription());
        habit.setTarget(request.getTarget());
        log.info("Привычка {} обновлена", id);
        return mapToResponse(habit);
    }
    // Удаление привычки
    public void delete (Long id) {
        // Проверка на наличие привычки
        if (!habitRepository.containsKey(id)) {
            throw new HabitNotFoundException(id);
        }
        habitRepository.remove(id);
    }

    // Entity в dto
    private HabitResponse mapToResponse(Habit habit) {
        HabitResponse res = new HabitResponse();
        res.setId(habit.getId());
        res.setName(habit.getName());
        res.setDescription(habit.getDescription());
        res.setTarget(habit.getTarget());
        return res;
    }

    // Возвращает Entity, чтобы RecordService проверял существование
    public Habit getHabitEntity(Long id) {
        Habit habit = habitRepository.get(id);
        if (habit == null) throw new HabitNotFoundException(id);
        return habit;
    }

    // Проверка существования
    public boolean existsById(Long id) {
        return habitRepository.containsKey(id);
    }
}

