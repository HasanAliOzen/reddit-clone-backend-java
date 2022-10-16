package com.travula.model;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString

@Entity
@Table(name = "token")
public class VerificationToken extends BaseEntity{

    @Column(name = "token",unique = true,nullable = false)
    private String token;

    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(name = "expiry_date",nullable = false)
    private Instant expiryDate;
}
