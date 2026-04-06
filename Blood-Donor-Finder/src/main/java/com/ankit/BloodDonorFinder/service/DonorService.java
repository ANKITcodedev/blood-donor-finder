package com.ankit.BloodDonorFinder.service;

import com.ankit.BloodDonorFinder.exception.BadRequestException;
import com.ankit.BloodDonorFinder.exception.ResourceNotFoundException;
import com.ankit.BloodDonorFinder.model.*;
import com.ankit.BloodDonorFinder.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DonorService {

    private final UserRepository userRepository;
    private final DonorProfileRepository donorProfileRepository;
    private final DonationHistoryRepository donationHistoryRepository;
    private final DonorRewardRepository donorRewardRepository;
    private final DonorResponseRepository donorResponseRepository;
    private final BloodRequestRepository bloodRequestRepository;
    @Autowired
    private SearchService searchService;

    // register donor
    public String registerDonor(Long userId, String bloodGroup) {

        // find user
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found!"));

        // check already register as donor
        if(donorProfileRepository.existsByUser(user)) {
            throw new BadRequestException("Already registered as donor!");
        }

        // create donor profile
        DonorProfile donor = new DonorProfile();
        donor.setUser(user);
        donor.setBloodGroup(DonorProfile.BloodGroup.valueOf(bloodGroup));
        donor.setAvailable(true);
        donor.setVerified(false);
        donor.setTotalDonations(0);

        // save donor
        donorProfileRepository.save(donor);

        // create reward entry for donor
        DonorReward donorReward = new DonorReward();
        donorReward.setDonor(donor);
        donorReward.setPoints(0);
        donorReward.setTotalDonations(0);
        donorReward.setBadge(DonorReward.Badge.NEWCOMER);

        donorRewardRepository.save(donorReward);

        return "Donor registered successfully!";
    }


    // toggle availability on/off
    public String toggleAvailability(Long donorId) {

        DonorProfile donor = donorProfileRepository.findById(donorId).orElseThrow(() -> new ResourceNotFoundException("Donor Not Found"));

        // if available then make it as unavailable or vice versa
        donor.setAvailable(!donor.isAvailable());
        donorProfileRepository.save(donor);

        return donor.isAvailable() ? "You are now AVAILABLE for donations" : "You are now UNAVAILABLE for donations";
    }

    // update location
    public String updateLocation(Long donorId, Double latitude, Double longitude) {

        DonorProfile donor = donorProfileRepository.findById(donorId).orElseThrow(() -> new RuntimeException("Donor not found!"));
        donor.setLatitude(latitude);
        donor.setLongitude(longitude);
        donorProfileRepository.save(donor);

        // clear cache so new location is reflected
        searchService.clearDonorCache();

        System.out.println("Cache cleared after location update!");

        return "Location Updated Successfully";
    }

    // get donation history
    public List<DonationHistory> getDonationHistory(Long donorId) {
        DonorProfile donor = donorProfileRepository.findById(donorId).orElseThrow(() -> new RuntimeException("Donor not found!"));

        return donationHistoryRepository.findByDonor(donor);
    }

    // get rewards
    public DonorReward getRewards(Long donorId) {

        DonorProfile donor = donorProfileRepository.findById(donorId).orElseThrow(() -> new RuntimeException("Donor not found!"));

        return donorRewardRepository.findByDonor(donor).orElseThrow(() -> new RuntimeException("Rewards not found!"));
    }

    // respond to blood request
    public String respondToRequest(Long donorId, Long requestId, String status) {

        // find donor
        DonorProfile donor = donorProfileRepository.findById(donorId).orElseThrow(() -> new RuntimeException("Donor not found!"));

        // find blood request
        BloodRequest request = bloodRequestRepository.findById(requestId).orElseThrow(() -> new RuntimeException("Request not found!"));

        //check already responded
        if(donorResponseRepository.findByBloodRequestAndDonor(request, donor) .isPresent()) {
            throw new BadRequestException("Already responded to this request!");
        }

        // save response
        DonorResponse donorResponse = new DonorResponse();
        donorResponse.setBloodRequest(request);
        donorResponse.setDonor(donor);
        donorResponse.setStatus(DonorResponse.Status.valueOf(status));

        donorResponseRepository.save(donorResponse);

        return "Response saved: " + status;
    }

}
