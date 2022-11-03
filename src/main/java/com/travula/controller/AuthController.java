package com.travula.controller;

import com.travula.dto.AuthenticationResponse;
import com.travula.dto.LoginRequest;
import com.travula.dto.RefreshTokenRequest;
import com.travula.dto.RegisterRequest;
import com.travula.service.AuthService;
import com.travula.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

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

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest){
        return ResponseEntity.ok(authService.refreshToken(refreshTokenRequest));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest){
        refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());
        return ResponseEntity.status(HttpStatus.OK).body("Refresh Token Deleted Successfully!!");
    }
}
