package com.ankit.BloodDonorFinder.controller;

import com.ankit.BloodDonorFinder.dto.AuthResponse;
import com.ankit.BloodDonorFinder.dto.LoginRequest;
import com.ankit.BloodDonorFinder.dto.RegisterRequest;
import com.ankit.BloodDonorFinder.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication",
        description = "Register and login APIs")
public class AuthController {

    private final AuthService authService;

    @Operation(
            summary = "Register new user",
            description = "Register as DONOR, SEEKER or ADMIN. " +
                    "Returns success message on registration."
    )
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        String  result = authService.register(request);
        return ResponseEntity.ok(result);
    }

    @Operation(
            summary = "Login user",
            description = "Login with email and password. " +
                    "Returns JWT token to use in other APIs."
    )
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}
