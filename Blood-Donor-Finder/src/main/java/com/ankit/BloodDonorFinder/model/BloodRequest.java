package com.ankit.BloodDonorFinder.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "blood_request")
@Data
public class BloodRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "seeker_user_id", nullable = false)
    private User seekerUser;

    @Column(nullable = false)
    private String patientName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DonorProfile.BloodGroup bloodGroup;

    @Column(nullable = false)
    private int unitsNeeded;

    @Column(nullable = false)
    private String hospitalName;

    private String hospitalAddress;

    private double latitude;
    private double longitude;

    @Enumerated(EnumType.STRING)
    private Urgency urgency;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String contactNumber;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(type = "string", example = "2024-12-31T10:00:00", description = "Date and time when blood is needed")
    private LocalDateTime neededByDate;
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        status = Status.OPEN;
    }

    public enum Urgency {
        CRITICAL, URGENT, NORMAL
    }

    public enum Status {
        OPEN, FULFILLED, EXPIRED
    }
}
