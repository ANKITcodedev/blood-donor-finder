package com.ankit.BloodDonorFinder.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String phone;
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String city;
    private String state;
    private String pincode;

    private LocalDateTime createdAt;

    @PrePersist
    public void prepersist() {
        createdAt = LocalDateTime.now();
    }

    public enum Role {
        DONOR, SEEKER, ADMIN
    }

}
