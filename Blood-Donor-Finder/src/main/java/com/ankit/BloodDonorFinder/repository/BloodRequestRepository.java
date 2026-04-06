package com.ankit.BloodDonorFinder.repository;

import com.ankit.BloodDonorFinder.model.BloodRequest;
import com.ankit.BloodDonorFinder.model.DonorProfile;
import com.ankit.BloodDonorFinder.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BloodRequestRepository extends JpaRepository<BloodRequest, Long> {

    List<BloodRequest> findBySeekerUser(User user);

    List<BloodRequest> findByStatus(BloodRequest.Status status);

    List<BloodRequest> findByBloodGroupAndStatus(
            DonorProfile.BloodGroup bloodGroup,
            BloodRequest.Status status
    );
}
