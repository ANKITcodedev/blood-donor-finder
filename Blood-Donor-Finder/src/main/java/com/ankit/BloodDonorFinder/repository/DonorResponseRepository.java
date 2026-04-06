package com.ankit.BloodDonorFinder.repository;

import com.ankit.BloodDonorFinder.model.BloodRequest;
import com.ankit.BloodDonorFinder.model.DonorProfile;
import com.ankit.BloodDonorFinder.model.DonorResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DonorResponseRepository extends JpaRepository<DonorResponse, Long> {

    List<DonorResponse> findByBloodRequest(BloodRequest request);

    List<DonorResponse> findByDonor(DonorProfile donor);

    Optional<DonorResponse> findByBloodRequestAndDonor(
            BloodRequest request,
            DonorProfile donor
    );
}
