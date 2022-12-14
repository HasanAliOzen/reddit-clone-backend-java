package com.travula.dto;

import lombok.*;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class AuthenticationResponse {
    private String authenticationToken;
    private Instant expiresAt;
    private String refreshToken;
    private String username;
}
