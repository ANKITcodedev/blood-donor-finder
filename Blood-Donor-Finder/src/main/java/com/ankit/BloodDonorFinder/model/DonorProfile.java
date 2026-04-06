package com.ankit.BloodDonorFinder.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "donor_profiles")
@Data
public class DonorProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BloodGroup bloodGroup;

    private LocalDate lastDonatedAt;

    private boolean isAvailable = true;
    private boolean isVerified = false;

    private double latitude;
    private double longitude;

    private int totalDonations = 0;

    public enum BloodGroup {
        A_POSITIVE, A_NEGATIVE,
        B_POSITIVE, B_NEGATIVE,
        O_POSITIVE, O_NEGATIVE,
        AB_POSITIVE, AB_NEGATIVE
    }
}
