package com.travula.model;

import lombok.*;

import javax.persistence.Entity;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder

@Entity
public class RefreshToken extends BaseEntity{
    private String token;
}
