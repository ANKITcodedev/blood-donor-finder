package com.ankit.BloodDonorFinder.repository;

import com.ankit.BloodDonorFinder.model.DonationHistory;
import com.ankit.BloodDonorFinder.model.DonorProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DonationHistoryRepository extends JpaRepository<DonationHistory, Long>{

    List<DonationHistory> findByDonor(DonorProfile donor);

    Optional<DonationHistory> findTopByDonorOrderByDonatedAtDesc(
        DonorProfile donor
    );
}
