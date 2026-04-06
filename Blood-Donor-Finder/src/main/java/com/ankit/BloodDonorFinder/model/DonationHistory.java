package com.ankit.BloodDonorFinder.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "donation_history")
@Data
public class DonationHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "request_id", nullable = false)
    private BloodRequest bloodRequest;

    @ManyToOne
    @JoinColumn(name = "donor_id", nullable = false)
    private DonorProfile donor;

    private String hospitalName;

    private LocalDateTime donatedAt;

    private boolean verifiedBySeeker = false;

    @PrePersist
    public void prePersist() {
        donatedAt = LocalDateTime.now();
    }
}
