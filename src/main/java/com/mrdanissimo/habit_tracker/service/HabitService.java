package com.mrdanissimo.habit_tracker.service;

import com.mrdanissimo.habit_tracker.dto.HabitRequest;
import com.mrdanissimo.habit_tracker.dto.HabitResponse;
import com.mrdanissimo.habit_tracker.entity.Habit;
import com.mrdanissimo.habit_tracker.exception.HabitNotFoundException;
import com.mrdanissimo.habit_tracker.repository.HabitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HabitService {
    private final HabitRepository habitRepository;

    // Создание новой привычки
    @Transactional
    public HabitResponse create(HabitRequest request) {
        // Превращаем DTO в Entity
        Habit habit = new Habit();
        habit.setName(request.getName());
        habit.setDescription(request.getDescription());
        habit.setTarget(request.getTarget());
        habit.setCreatedAt(LocalDateTime.now());

        // Сохраняем в БД
        Habit savedHabit = habitRepository.save(habit);

        // Превращаем Entity обратно в DTO и отдаем контроллеру
        return mapToResponse(savedHabit);
    }

    // Получение привычки по ID, если нет, то выбрасывает ошибку
    @Transactional(readOnly = true)
    public Habit getHabitEntity(Long id) {
        return habitRepository.findById(id).orElseThrow(() -> new HabitNotFoundException(id));
    }

    // Получение всех привычек
    @Transactional(readOnly = true)
    public List<HabitResponse> getAll() {
        return habitRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    // Проверка существования
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return habitRepository.existsById(id);
    }

    // Поиск по id
    @Transactional(readOnly = true)
    public HabitResponse getById(Long id) {
        Habit habit = getHabitEntity(id);
        return mapToResponse(habit);
    }

    @Transactional
    public void delete(Long id) {
        if (!existsById(id)) {
            throw new HabitNotFoundException(id);
        }
        habitRepository.deleteById(id);
    }

    // Обновление привычки
    @Transactional
    public HabitResponse update(Long id, HabitRequest request) {
        Habit habit = getHabitEntity(id);

        habit.setName(request.getName());
        habit.setDescription(request.getDescription());
        habit.setTarget(request.getTarget());

        Habit updatedHabit = habitRepository.save(habit);
        return mapToResponse(updatedHabit);
    }

    private HabitResponse mapToResponse(Habit habit) {
        HabitResponse res = new HabitResponse();
        res.setId(habit.getId());
        res.setName(habit.getName());
        res.setDescription(habit.getDescription());
        res.setTarget(habit.getTarget());
        return res;
    }
}

