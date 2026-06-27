package com.mrdanissimo.habit_tracker.service;

import com.mrdanissimo.habit_tracker.dto.*;
import com.mrdanissimo.habit_tracker.entity.Habit;
import com.mrdanissimo.habit_tracker.entity.Record;
import com.mrdanissimo.habit_tracker.repository.RecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatsService {
    private final RecordRepository recordRepository;
    private final HabitService habitService;

    public StatsResponse getStats(Long habitId) {
        // Проверка существования привычки
        Habit habit = habitService.getHabitEntity(habitId);

        List<Record>records = recordRepository.findAllByHabitId(habitId);
        Set<LocalDate> completedDates = records.stream().map(Record::getDate).collect(Collectors.toSet());

        StatsResponse stats = new StatsResponse();
        stats.setTotalCompletion(completedDates.size());
        stats.setCurrentStreak(calculateCurrentStreak(completedDates));
        stats.setBestStreak(calculateBestStreak(completedDates));
        stats.setCompletionRate(calculateCompletionRate(habit.getCreatedAt().toLocalDate(), completedDates.size()));

        return stats;
    }

    public int calculateCurrentStreak(Set<LocalDate> dates) {
        int streak = 0;
        LocalDate checkDate = LocalDate.now();

        if (!dates.contains(checkDate) && dates.contains(checkDate.minusDays(1))) {
            checkDate = checkDate.minusDays(1);
        }

        while (dates.contains(checkDate)) {
            streak++;
            checkDate = checkDate.minusDays(1);
        }
        return streak;
    }

    public int calculateBestStreak(Set<LocalDate> dates) {
        if (dates.isEmpty()) return 0;

        // Сортируем даты по возрастанию
        List<LocalDate> sortedDates = dates.stream().sorted().toList();

        int maxStreak = 1;
        int currentStreak = 1;

        for (int i = 1; i < sortedDates.size(); i ++) {
            // Если текущая дата идет ровно на след день после предыдущей
            if (sortedDates.get(i).minusDays(1).equals(sortedDates.get(i - 1))) {
                currentStreak++; maxStreak = Math.max(maxStreak, currentStreak);
            } else {currentStreak = 1;} // Серия прервалась, сбрасываем счетчик
        } return maxStreak;
    }
    public double calculateCompletionRate(LocalDate createdAt, int totalCompletions) {
        long daysSinceCreation = ChronoUnit.DAYS.between(createdAt, LocalDate.now()) + 1; // +1 чтоюы учесть день создания
        if (daysSinceCreation <= 0) return 0.0;

        // Считаем процент и умножаем на 100.0 чтобы получилось double
        double rate = (totalCompletions * 100.0) / daysSinceCreation;
        return Math.round(rate * 10.0) / 10.0; // Округляем до одного знака
    }

    // Сводка за сегодня
    public DailyStatsResponse getDailyStats() {
        LocalDate today = LocalDate.now();
        // Берем все привычки через твой готовый сервис
        List<HabitDailyStatus> statuses = habitService.getAll().stream()
                .map(h -> new HabitDailyStatus(
                        h.getId(),
                        h.getName(),
                        recordRepository.existsByHabitIdAndDate(h.getId(), today)))
                .toList();

        DailyStatsResponse response = new DailyStatsResponse();
        response.setDate(today);
        response.setHabits(statuses);
        return response;
    }

    // Прогресс за 7 дней
    public WeeklyStatsResponse getWeeklyStats() {
        List<DailyProgress> weekProgress = new java.util.ArrayList<>();
        int totalHabits = habitService.getAll().size(); // Общее количество привычек

        // Идем от 6 дней назад до сегодня
        for (int i = 6; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusDays(i);
            int completed = recordRepository.countByDate(date); // Наш новый метод из репозитория
            weekProgress.add(new DailyProgress(date, completed, totalHabits));
        }

        WeeklyStatsResponse response = new WeeklyStatsResponse();
        response.setWeek(weekProgress);
        return response;
    }
}
