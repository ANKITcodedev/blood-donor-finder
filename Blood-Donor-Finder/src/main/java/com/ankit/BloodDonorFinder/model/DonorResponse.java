package com.ankit.BloodDonorFinder.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "donor_responses")
@Data
public class DonorResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "request_id", nullable = false)
    private BloodRequest bloodRequest;

    @ManyToOne
    @JoinColumn(name = "donor_id", nullable = false)
    private DonorProfile donor;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime respondedAt;

    @PrePersist
    public void prePersist() {
        respondedAt = LocalDateTime.now();
        status = Status.PENDING;
    }

    public enum Status {
        PENDING, ACCEPTED, DECLINED
    }
}
