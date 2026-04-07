package com.ankit.BloodDonorFinder.service;

import com.ankit.BloodDonorFinder.model.BloodRequest;
import com.ankit.BloodDonorFinder.model.DonorProfile;
import com.ankit.BloodDonorFinder.repository.BloodRequestRepository;
import com.ankit.BloodDonorFinder.repository.DonorProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final DonorProfileRepository donorProfileRepository;
    private final BloodRequestRepository bloodRequestRepository;

    // find nearby donors by blood group and radius
    // @Cacheable(value = "nearbyDonors", key = "#bloodGroup + #latitude + #longitude + #radiusKm")
    public List<DonorProfile> findNearByDonors(String bloodGroup, Double latitude, Double longitude, Double radiusKm) {

        System.out.println("Fatching from MySQL - not from cache");

        // get all available donors with matching bloodgroup
        List<DonorProfile> availableDonors = donorProfileRepository.findByBloodGroupAndIsAvailable(DonorProfile.BloodGroup.valueOf(bloodGroup), true);

        // filter by distance
        List<DonorProfile> nearByDonors = new ArrayList<>();

        for(DonorProfile donor : availableDonors) {

            // skip donors with no location
            if(donor.getLatitude() == 0.0 || donor.getLongitude() == 0.0) {
                continue;
            }

            // calculate distance using harversine
            double distance = calculateDistance(latitude, longitude, donor.getLatitude(), donor.getLongitude());

            // add donor if within radius
            if(distance <= radiusKm) {
                nearByDonors.add(donor);
            }
        }
         return nearByDonors;
    }

    // find donors for a specific blood request
    // @Cacheable(value = "requestDonors",
         //   key = "#requestId + #radiusKm")
    public List<DonorProfile> findDonorsForRequest(Long requestId, Double radiusKm) {

        // find the blood request
        BloodRequest request = bloodRequestRepository.findById(requestId).orElseThrow(() -> new RuntimeException("Request not found!"));

        // find compatible blood request
        return findNearByDonors(request.getBloodGroup().name(), request.getLatitude(), request.getLongitude(), radiusKm);
    }

    // haversine formula
    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {

        // earth radius in km
        double R = 6371;

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(
                Math.sqrt(a), Math.sqrt(1 - a));

        // Distance in km
        return R * c;
    }

    // @CacheEvict(value = "nearbyDonors", allEntries = true)
    public void clearDonorCache() {
        System.out.println("Donor cache cleared!");
    }
}
