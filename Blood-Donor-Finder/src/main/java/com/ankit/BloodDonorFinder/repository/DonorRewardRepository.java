package com.ankit.BloodDonorFinder.repository;

import com.ankit.BloodDonorFinder.model.DonorProfile;
import com.ankit.BloodDonorFinder.model.DonorReward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DonorRewardRepository extends JpaRepository<DonorReward, Long> {

    Optional<DonorReward> findByDonor(DonorProfile donor);
}
