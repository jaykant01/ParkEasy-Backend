package com.jay.ParkEasy_Backend.service;

import com.jay.ParkEasy_Backend.dto.auth.AuthResponse;
import com.jay.ParkEasy_Backend.dto.auth.LoginRequest;
import com.jay.ParkEasy_Backend.dto.auth.RegisterRequest;
import com.jay.ParkEasy_Backend.entity.Role;
import com.jay.ParkEasy_Backend.entity.UserEntity;
import com.jay.ParkEasy_Backend.jwt.JwtService;
import com.jay.ParkEasy_Backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponse register(RegisterRequest req) {

        UserEntity user = UserEntity.builder()
                .name(req.getName())
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .role(Role.USER)   // default
                .build();

        userRepository.save(user);

        return new AuthResponse(jwtService.generateToken(user));
    }

    public AuthResponse login(LoginRequest req) {

        UserEntity user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getPassword() == null) {
            throw new RuntimeException("This account uses Google Sign-In. Please login with Google.");
        }

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return new AuthResponse(jwtService.generateToken(user));
    }

}
