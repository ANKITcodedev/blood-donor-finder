package com.ankit.BloodDonorFinder.service;

import com.ankit.BloodDonorFinder.model.DonorProfile;
import com.ankit.BloodDonorFinder.model.DonorReward;
import com.ankit.BloodDonorFinder.repository.DonorProfileRepository;
import com.ankit.BloodDonorFinder.repository.DonorRewardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RewardService {

    private final DonorRewardRepository donorRewardRepository;
    private final DonorProfileRepository donorProfileRepository;

    // add points after donation
    public void addPoints(Long donorId, int points) {

        // find donor
        DonorProfile donor = donorProfileRepository.findById(donorId).orElseThrow(() -> new RuntimeException("Donor not found!"));

        // find donor reward
        DonorReward reward = donorRewardRepository.findByDonor(donor).orElseThrow(() -> new RuntimeException("Reward not Found!"));

        // add points
        reward.setPoints(reward.getPoints() + points);

        // increment total donation
        reward.setTotalDonations(reward.getTotalDonations() + 1);

        //update badge
        updateBadge(reward);

        donorRewardRepository.save(reward);
    }

    // update badge based on total donation
    private void updateBadge(DonorReward reward) {

        int totalDonations = reward.getTotalDonations();

        if(totalDonations >= 20) {
            reward.setBadge(DonorReward.Badge.LEGEND);
        } else if (totalDonations >= 10) {
            reward.setBadge(DonorReward.Badge.HERO);
        } else if (totalDonations >= 5) {
            reward.setBadge(DonorReward.Badge.LIFE_SAVER);
        } else {
            reward.setBadge(DonorReward.Badge.NEWCOMER);
        }
    }

    // get rewards for donor
    public DonorReward getRewards(Long donorId) {

        DonorProfile donor = donorProfileRepository.findById(donorId).orElseThrow(() -> new RuntimeException("Donor not found!"));

        return donorRewardRepository.findByDonor(donor).orElseThrow(() ->new RuntimeException("Reward not found!"));
    }

    // get points based on urgency
    public int getPointsForDonation(String urgency) {

        switch (urgency) {
            case "CRITICAL": return 200; // 2x points
            case "URGENT": return 150; // 1.5x points
            default: return 100; // normal points
        }
    }
}
