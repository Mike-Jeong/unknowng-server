package com.example.unknowngserver.auth.controller;

import com.example.unknowngserver.auth.dto.LoginRequest;
import com.example.unknowngserver.auth.service.AuthService;
import com.example.unknowngserver.common.dto.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Response> login(@Valid @RequestBody LoginRequest loginRequest) {
        authService.login(loginRequest);
        return ResponseEntity.ok().body(Response.ok());
    }

    @PostMapping("/logout")
    public ResponseEntity<Response> logout() {
        authService.logout();
        return ResponseEntity.ok().body(Response.ok());
    }
}
