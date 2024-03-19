package com.example.bookstore.controller;

import com.example.bookstore.dto.auth.LoginRequestDTO;
import com.example.bookstore.dto.auth.RegisterRequestDTO;
import com.example.bookstore.dto.auth.ResponseTokenDTO;
import com.example.bookstore.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author oksanapoliakova on 15.03.2024
 * @projectName BookStore
 */

/**
 * The `AuthController` class defines endpoints for user registration and login.
 */

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @Operation(summary = "Possibility to register an user")
    public ResponseEntity<ResponseTokenDTO> register(@RequestBody RegisterRequestDTO request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    @Operation(summary = "Possibility to login")
    public ResponseEntity<ResponseTokenDTO> login(@RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
