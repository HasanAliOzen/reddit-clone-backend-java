package com.travula.controller;

import com.travula.dto.AuthenticationResponse;
import com.travula.dto.LoginRequest;
import com.travula.dto.RegisterRequest;
import com.travula.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<String> signup(@RequestBody RegisterRequest registerRequest){
        authService.signup(registerRequest);
        return ResponseEntity.ok("User registration Successful");
    }

    @GetMapping("/account-verification/{token}")
    public ResponseEntity<String> emailVerification(@PathVariable String token){
        authService.verifyToken(token);
        return ResponseEntity.ok("Account verified successfully!!!");
    }

    @PutMapping("/refresh-token/{id}")
    public ResponseEntity<String> refreshToken(@PathVariable Long id){
        authService.updateVerificationToken(id);
        return ResponseEntity.ok("Verification email send!");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok(authService.login(loginRequest));
    }


}
