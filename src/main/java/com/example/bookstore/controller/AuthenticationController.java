package com.example.bookstore.controller;

import com.example.bookstore.security.dto.RefreshTokenRequestDTO;
import com.example.bookstore.service.AuthenticationService;
import com.example.bookstore.service.dto.UserRegistrationDTO;
import com.example.bookstore.service.dto.UserRegistrationResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.example.bookstore.security.dto.LoginRequestDTO;
import com.example.bookstore.security.dto.LoginResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequestDto) {
        return ResponseEntity.ok(authenticationService.authenticate(loginRequestDto));
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDTO> refresh(@Valid @RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO) {
        return ResponseEntity.ok(authenticationService.refresh(refreshTokenRequestDTO));
    }

    @PostMapping("/register")
    public ResponseEntity<UserRegistrationResponseDTO> register(@Valid @RequestBody UserRegistrationDTO userRegistrationDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authenticationService.register(userRegistrationDTO));
    }

}
