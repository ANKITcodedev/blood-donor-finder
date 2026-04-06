package com.ankit.BloodDonorFinder.service;

import com.ankit.BloodDonorFinder.exception.BadRequestException;
import com.ankit.BloodDonorFinder.exception.ResourceNotFoundException;
import com.ankit.BloodDonorFinder.model.BloodRequest;
import com.ankit.BloodDonorFinder.model.DonorProfile;
import com.ankit.BloodDonorFinder.model.User;
import com.ankit.BloodDonorFinder.repository.BloodRequestRepository;
import com.ankit.BloodDonorFinder.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BloodRequestService {

    private final BloodRequestRepository bloodRequestRepository;
    private final UserRepository userRepository;

    // create new blood request
    public String createRequest(
            Long seekerId, String patientName,
            String bloodgroup, int unitsNeeded,
            String hospitalName, String hospitalAddress,
            String urgency, String contactNumber,
            double latitude, double longitude) {

        // find seeker
        User seeker = userRepository.findById(seekerId).orElseThrow(() -> new RuntimeException("User not found!"));

        // create blood request
        BloodRequest request = new BloodRequest();
        request.setSeekerUser(seeker);
        request.setPatientName(patientName);
        request.setBloodGroup(DonorProfile.BloodGroup.valueOf(bloodgroup));
        request.setUnitsNeeded(unitsNeeded);
        request.setHospitalName(hospitalName);
        request.setHospitalAddress(hospitalAddress);
        request.setUrgency(BloodRequest.Urgency.valueOf(urgency));
        request.setContactNumber(contactNumber);
        request.setLatitude(latitude);
        request.setLongitude(longitude);

        // save request
        bloodRequestRepository.save(request);

        return "Blood Request created successfully";
    }

    // fulfill a request
    public String fulfillRequest(Long requestId) {

        BloodRequest request = bloodRequestRepository.findById(requestId).orElseThrow(() -> new ResourceNotFoundException("Request not found!"));

        // check already fulfilled
        if(request.getStatus() == BloodRequest.Status.FULFILLED) {
            throw new BadRequestException("Request already fulfilled!");
        }

        request.setStatus(BloodRequest.Status.FULFILLED);

        bloodRequestRepository.save(request);

        return "Request Marked as fulfilled";
    }

    // cancel a request
    public String cancelRequest(Long requestId) {

        BloodRequest request = bloodRequestRepository.findById(requestId).orElseThrow(() -> new RuntimeException("Request not found!"));

        // check already fulfilled
        if(request.getStatus() == BloodRequest.Status.FULFILLED) {
            throw new BadRequestException("Cannot cancel fulfilled request!");
        }

        bloodRequestRepository.delete(request);

        return "Request cancelled successfully!";
    }

    // get my posted request
    public List<BloodRequest> getMyRequests(Long seekerId) {

        User seeker = userRepository.findById(seekerId).orElseThrow(() -> new RuntimeException("User not found!"));

        return bloodRequestRepository.findBySeekerUser(seeker);
    }

    // get all open request
    public List<BloodRequest> getAllOpenRequest() {

        return bloodRequestRepository.findByStatus(BloodRequest.Status.OPEN);
    }
}
