package com.ankit.BloodDonorFinder.repository;

import com.ankit.BloodDonorFinder.model.DonorProfile;
import com.ankit.BloodDonorFinder.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DonorProfileRepository extends JpaRepository<DonorProfile, Long> {

    Optional<DonorProfile> findByUser(User user);

    List<DonorProfile> findByBloodGroupAndIsAvailable(
            DonorProfile.BloodGroup bloodGroup,
            boolean isAvailable
    );

    boolean existsByUser(User user);
}
