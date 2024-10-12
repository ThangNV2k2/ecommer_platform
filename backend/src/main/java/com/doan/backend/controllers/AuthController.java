package com.doan.backend.controllers;

import com.doan.backend.dto.request.LoginEmailRequest;
import com.doan.backend.dto.request.RegisterRequest;
import com.doan.backend.dto.response.ApiResponse;
import com.doan.backend.dto.response.JwtResponse;
import com.doan.backend.dto.response.UserResponse;
import com.doan.backend.services.AuthService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {
    AuthService authService;

    @PostMapping("/register")
    ApiResponse<UserResponse> register(@RequestBody RegisterRequest registerRequest) {
        return authService.registerWithEmail(registerRequest);
    }

    @PostMapping("/login")
    ApiResponse<JwtResponse> login(@RequestBody LoginEmailRequest loginEmailRequest) {
        return authService.loginWithEmail(loginEmailRequest);
    }

    @PostMapping("/login/google")
    ApiResponse<JwtResponse> loginWithGoogle(@RequestParam String token) throws Exception {
        return authService.loginWithGoogle(token);
    }

    @GetMapping("/get-user")
    ApiResponse<UserResponse> getUser() {
        return authService.getUser();
    }
}
