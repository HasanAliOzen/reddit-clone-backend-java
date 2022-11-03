package com.travula.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class RefreshTokenRequest {
    @NotBlank
    private String refreshToken;
    private String username;
}
