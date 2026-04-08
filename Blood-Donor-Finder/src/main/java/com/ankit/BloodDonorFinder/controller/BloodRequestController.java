package com.ankit.BloodDonorFinder.controller;

import com.ankit.BloodDonorFinder.service.BloodRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/requests")
@RequiredArgsConstructor
@Tag(name = "Blood Requests",
        description = "Post and manage emergency blood requests")
public class BloodRequestController {

    private final BloodRequestService bloodRequestService;

    // post new blood request
    @Operation(
            summary = "Create blood request",
            description = "Post an emergency blood request. " +
                    "Provide patient details, hospital location and blood group needed."
    )
    @PostMapping("/create")
    public ResponseEntity<String> createRequest(
            @RequestParam Long seekerId,
            @RequestParam String patientName,
            @RequestParam String bloodGroup,
            @RequestParam int unitsNeeded,
            @RequestParam String hospitalName,
            @RequestParam String hospitalAddress,
            @RequestParam String urgency,
            @RequestParam String contactNumber,
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam(required = false) String neededByDate) {

        return ResponseEntity.ok(bloodRequestService.createRequest(
                seekerId, patientName, bloodGroup, unitsNeeded, hospitalName,
                hospitalAddress, urgency, contactNumber, latitude, longitude, neededByDate)
        );
    }

    // mark request as fulfilled
    @Operation(
            summary = "Fulfill request",
            description = "Mark a blood request as fulfilled " +
                    "when blood has been arranged."
    )
    @PutMapping("/fulfill/{requestId}")
    public ResponseEntity<String> fulfillRequest(@PathVariable Long requestId) {

        return ResponseEntity.ok(bloodRequestService.fulfillRequest(requestId));
    }

    // cancel request
    @Operation(
            summary = "Cancel request",
            description = "Cancel an open blood request. " +
                    "Cannot cancel already fulfilled requests."
    )
    @DeleteMapping("/cancel/{requestId}")
    public ResponseEntity<String> cancelRequest(@PathVariable Long requestId) {

        return ResponseEntity.ok(bloodRequestService.cancelRequest(requestId));
    }

    // get my posted requests
    @Operation(
            summary = "My requests",
            description = "Get all blood requests posted by this user."
    )
    @GetMapping("/my/{seekerId}")
    public ResponseEntity<?> getMyRequest(@PathVariable Long seekerId) {

        return ResponseEntity.ok(bloodRequestService.getMyRequests(seekerId));
    }

    @Operation(
            summary = "All 'OPEN' requests",
            description = "Get all currently open blood requests in the system. " +
                    "Donors can see and respond to these."
    )
    @GetMapping("/open")
    public ResponseEntity<?> getAllOpenRequest() {

        return ResponseEntity.ok(bloodRequestService.getAllOpenRequest());
    }

}
