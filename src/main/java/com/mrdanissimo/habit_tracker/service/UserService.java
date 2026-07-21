package com.mrdanissimo.habit_tracker.service;

import com.mrdanissimo.habit_tracker.dto.AuthRequest;
import com.mrdanissimo.habit_tracker.dto.AuthResponse;
import com.mrdanissimo.habit_tracker.dto.UserResponse;
import com.mrdanissimo.habit_tracker.entity.User;
import com.mrdanissimo.habit_tracker.exception.DuplicateUserException;
import com.mrdanissimo.habit_tracker.exception.InvalidCredentialsException;
import com.mrdanissimo.habit_tracker.exception.UserNotFoundException;
import com.mrdanissimo.habit_tracker.mapper.UserMapper;
import com.mrdanissimo.habit_tracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    public User getCurrentUser() {
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    public Long getCurrentUserId() {
        return getCurrentUser().getId();
    }

    // Регистрация
    public UserResponse register(AuthRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new DuplicateUserException(request.getUsername());
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());

        userRepository.save(user);
        return userMapper.toResponse(user);
    }

    // Логин
    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        String token = jwtService.generateToken(user.getUsername());
        return new AuthResponse(token, user.getUsername());
    }
}