package com.travula.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString

@Entity
@Table(name = "users")
public class User extends BaseEntity{
    @NotBlank(message = "Username is required!!!")
    @Column(name = "username",nullable = false,unique = true)
    private String username;

    @NotBlank(message = "Password is required!!!")
    @Column(name = "password",nullable = false)
    private String password;

    @Email
    @NotBlank(message = "Email is required!!!")
    @Column(name = "email",nullable = false,unique = true)
    private String email;

    @Column(name = "enabled",nullable = false)
    private boolean enabled;
}
