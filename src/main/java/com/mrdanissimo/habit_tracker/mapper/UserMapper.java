package com.mrdanissimo.habit_tracker.mapper;

import com.mrdanissimo.habit_tracker.dto.UserResponse;
import com.mrdanissimo.habit_tracker.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toResponse(User user) {
        if (user == null) {
            return null;
        }
        // Используем конструктор из @AllArgsConstructor
        return new UserResponse(user.getUsername());
    }
}