package com.ankit.BloodDonorFinder.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "donor_rewards")
@Data
public class DonorReward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @JoinColumn(name = "donor_id", nullable = false)
    private DonorProfile donor;

    private int points = 0;
    private int totalDonations = 0;

    @Enumerated(EnumType.STRING)
    private Badge badge;

    private LocalDateTime lastUpdated;

    @PrePersist
    public void prePersist() {
        lastUpdated = LocalDateTime.now();
    }

    public enum Badge {
        NEWCOMER,   // 1st donation
        LIFE_SAVER,   // 5th donation
        HERO,   // 10th donation
        LEGEND   // 20th donation
    }
}
