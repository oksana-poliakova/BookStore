package com.example.bookstore.service;

import com.example.bookstore.dto.auth.LoginRequestDTO;
import com.example.bookstore.dto.auth.RegisterRequestDTO;
import com.example.bookstore.dto.auth.ResponseTokenDTO;
import com.example.bookstore.entity.Role;
import com.example.bookstore.entity.User;
import com.example.bookstore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author oksanapoliakova on 15.03.2024
 * @projectName BookStore
 */

/**
 * The `AuthService` class handles the user registration and login logic, using the `UserRepository`,
 * `PasswordEncoder`, and `JWTTokenService`.
 */
@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTTokenService jwtService;
    private final AuthenticationManager authenticationManager;

    @Autowired

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JWTTokenService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    // User registration logic
    public ResponseTokenDTO register(RegisterRequestDTO request) {
        var user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        userRepository.save(user);
        return jwtService.generateTokens(user);
    }

    // User login logic
    public ResponseTokenDTO login(LoginRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByUsername(request.getUsername()).orElseThrow();
        return jwtService.generateTokens(user);
    }
}
