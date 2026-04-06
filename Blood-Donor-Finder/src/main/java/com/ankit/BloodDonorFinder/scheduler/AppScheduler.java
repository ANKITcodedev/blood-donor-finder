package com.ankit.BloodDonorFinder.scheduler;

import com.ankit.BloodDonorFinder.model.BloodRequest;
import com.ankit.BloodDonorFinder.model.DonationHistory;
import com.ankit.BloodDonorFinder.model.DonorProfile;
import com.ankit.BloodDonorFinder.model.Notification;
import com.ankit.BloodDonorFinder.repository.BloodRequestRepository;
import com.ankit.BloodDonorFinder.repository.DonationHistoryRepository;
import com.ankit.BloodDonorFinder.repository.DonorProfileRepository;
import com.ankit.BloodDonorFinder.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class AppScheduler {

    private final BloodRequestRepository bloodRequestRepository;
    private final DonorProfileRepository donorProfileRepository;
    private final DonationHistoryRepository donationHistoryRepository;
    private final NotificationService notificationService;

    // auto expire requests every day at midnight
    @Scheduled(cron = "0 0 0 * * *")
    public void expireAllRequest() {

        System.out.println("Running expiry check: " + LocalDateTime.now());

        // get all open request
        List<BloodRequest> openRequest = bloodRequestRepository.findByStatus(BloodRequest.Status.OPEN);

        for(BloodRequest request: openRequest) {

            // check if needed by date has passed
            if(request.getNeededByDate() != null && request.getNeededByDate().isBefore((LocalDateTime.now()))) {

                // mark as expired
                request.setStatus(BloodRequest.Status.EXPIRED);
                bloodRequestRepository.save(request);

                // notify seeker
                notificationService.sendNotification(
                        request.getSeekerUser(),
                        "Request Expired",
                        "Your blood request for " + request.getPatientName() + " has expired",
                        Notification.Type.REMINDER
                );

                System.out.println("Expired request id: " + request.getId());
            }
        }
    }

    // remind donors to donate every sunday at 10am
    @Scheduled(cron = "0 0 10 * * SUN")
    public void remindDonors() {

        System.out.println("Running donor reminders: " + LocalDateTime.now());

        // get all available donors
        List<DonorProfile> allDonors = donorProfileRepository.findAll();

        for(DonorProfile donor: allDonors) {

            //get last donation
            DonationHistory lastDonation = donationHistoryRepository.findTopByDonorOrderByDonatedAtDesc(donor).orElse(null);

            //check if donor is eligible (90 days gap)
            if(lastDonation == null) {

                // never donated remind them
                notificationService.sendNotification(
                        donor.getUser(),
                        "Donate Blood Today!",
                        "You have never donated blood. " + "Your donation can save life!",
                        Notification.Type.REMINDER
                );
            } else {

                // check 90 days gap
                long daysSinceLastDonation = ChronoUnit.DAYS.between(lastDonation.getDonatedAt().toLocalDate(), LocalDate.now());

                if(daysSinceLastDonation >= 90) {

                    // they are eligible and remind
                    notificationService.sendNotification(
                            donor.getUser(),
                            "You can donate Again!",
                            "It has been " + daysSinceLastDonation +
                                    " days since your last donation. " +
                                    "You are eligible to donate Again!",
                            Notification.Type.REMINDER
                    );
                }
            }
        }
    }
}
