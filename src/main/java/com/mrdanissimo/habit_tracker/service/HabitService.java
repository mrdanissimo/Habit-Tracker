package com.mrdanissimo.habit_tracker.service;

import com.mrdanissimo.habit_tracker.dto.HabitRequest;
import com.mrdanissimo.habit_tracker.dto.HabitResponse;
import com.mrdanissimo.habit_tracker.entity.Habit;
import com.mrdanissimo.habit_tracker.entity.User;
import com.mrdanissimo.habit_tracker.exception.HabitNotFoundException;
import com.mrdanissimo.habit_tracker.mapper.HabitMapper;
import com.mrdanissimo.habit_tracker.repository.HabitRepository;
import com.mrdanissimo.habit_tracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HabitService {
    private final HabitRepository habitRepository;
    private final UserRepository userRepository;
    private final HabitMapper habitMapper;


    // Создание новой привычки
    public HabitResponse create(HabitRequest request) {
        Long userId = getCurrentUserId(); // Узнаем ID текущего пользователя
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        // Используем маппер для создания Entity
        Habit habit = habitMapper.toEntity(request);
        habit.setCreatedAt(LocalDateTime.now()); // Добавляем техническое поле даты создания
        habit.setUser(currentUser);

        Habit saved = habitRepository.save(habit);
        return habitMapper.toResponse(saved); // Используем маппер для ответа
    }

    // Получение привычки по ID, если нет, то выбрасывает ошибку
    @Transactional(readOnly = true)
    public Habit getHabitEntity(Long id) {
        return habitRepository.findById(id).orElseThrow(() -> new HabitNotFoundException(id));
    }

    // Получение всех привычек
    @Transactional(readOnly = true)
    public List<HabitResponse> getAllMyHabits() {
        Long userId = getCurrentUserId(); // Узнаем ID того, кто делает запрос
        return habitRepository.findAllByUserId(userId).stream()
                .map(habitMapper::toResponse).toList();
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
        return habitMapper.toResponse(habit);
    }

    @Transactional
    public void delete(Long habitId) {
        // Узнаем, кто пытается удалить
        Long currentUserId = getCurrentUserId();

        // Достаем привычку из базы
        Habit habit = habitRepository.findById(habitId)
                .orElseThrow(() -> new RuntimeException("Привычка не найдена"));

        // Проверка совпадения пользователя
        if (!habit.getUser().getId().equals(currentUserId)) {
            throw new RuntimeException("Доступ запрещен! Это не ваша привычка");
        }

        // Если ID совпали, то удаляем
        habitRepository.delete(habit);
    }

    // Обновление привычки
    @Transactional
    public HabitResponse update(Long id, HabitRequest request) {
        Habit habit = getHabitEntity(id);

        habit.setName(request.getName());
        habit.setDescription(request.getDescription());
        habit.setTarget(request.getTarget());

        Habit updatedHabit = habitRepository.save(habit);
        return habitMapper.toResponse(updatedHabit);
    }

    private Long getCurrentUserId() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow().getId();
    }
}

