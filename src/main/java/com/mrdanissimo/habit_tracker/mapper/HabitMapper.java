package com.mrdanissimo.habit_tracker.mapper;

import com.mrdanissimo.habit_tracker.dto.HabitRequest;
import com.mrdanissimo.habit_tracker.dto.HabitResponse;
import com.mrdanissimo.habit_tracker.entity.Habit;
import org.springframework.stereotype.Component;

@Component
public class HabitMapper {

    public Habit toEntity(HabitRequest request) {
        if (request == null) {
            return null;
        }
        Habit habit = new Habit();
        habit.setName(request.getName());
        habit.setDescription(request.getDescription());
        habit.setTarget(request.getTarget());
        return habit;
    }

    public HabitResponse toResponse(Habit habit) {
        if (habit == null) {
            return null;
        }
        HabitResponse response = new HabitResponse();
        response.setId(habit.getId());
        response.setName(habit.getName());
        response.setDescription(habit.getDescription());
        response.setTarget(habit.getTarget());
        return response;
    }
}