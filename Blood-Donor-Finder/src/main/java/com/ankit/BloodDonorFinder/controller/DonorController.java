package com.ankit.BloodDonorFinder.controller;

import com.ankit.BloodDonorFinder.service.DonorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/donor")
@RequiredArgsConstructor
@Tag(name = "Donor",
        description = "Manage donor profile, location and responses")
public class DonorController {

    @Autowired
    private DonorService donorService;


    // register as donor
    @Operation(
            summary = "Register as donor",
            description = "User registers as blood donor. " +
                    "Provide userId and blood group like O_POSITIVE, A_NEGATIVE etc."
    )
    @PostMapping("/register/{userId}")
    public ResponseEntity<String> registerDonor(@PathVariable Long userId, @RequestParam String bloodGroup) {
        return ResponseEntity.ok(donorService.registerDonor(userId, bloodGroup));
    }

    // toggle availability on/off
    @Operation(
            summary = "Toggle availability",
            description = "Turn donation availability ON or OFF. " +
                    "Unavailable donors won't receive blood requests."
    )
    @PutMapping("/availability/{donorId}")
    public ResponseEntity<String> toggleAvailability(@PathVariable Long donorId) {
        return ResponseEntity.ok(donorService.toggleAvailability(donorId));
    }

    //update location
    @Operation(
            summary = "Update location",
            description = "Update donor's current GPS location. " +
                    "Used to find donors near hospitals."
    )
    @PutMapping("/location/{donorId}")
    public ResponseEntity<String> updateLocation(@PathVariable Long donorId, @RequestParam Double latitude, @RequestParam Double longitude) {
        return ResponseEntity.ok(donorService.updateLocation(donorId, latitude, longitude));
    }

    // view donation history
    @Operation(
            summary = "View donation history",
            description = "Get list of all past donations by this donor."
    )
    @GetMapping("/history/{donorId}")
    public ResponseEntity<?> getDonationHistory(@PathVariable Long donorId) {
        return ResponseEntity.ok(donorService.getDonationHistory(donorId));
    }

    // view rewards
    @Operation(
            summary = "View rewards",
            description = "Get donor's points and badge. " +
                    "Badges: NEWCOMER, LIFE_SAVER, HERO, LEGEND"
    )
    @GetMapping("/rewards/{donorId}")
    public ResponseEntity<?> getRewards(@PathVariable Long donorId) {
        return ResponseEntity.ok(donorService.getRewards(donorId));
    }

    // accept or decline blood request
    @Operation(
            summary = "Respond to blood request",
            description = "Accept or decline a blood request. " +
                    "Status must be ACCEPTED or DECLINED."
    )
    @PostMapping("/respond/{donorId}/{requestId}")
    public ResponseEntity<String> respondToRequest(@PathVariable Long donorId, @PathVariable Long requestId, @RequestParam String status) {
        return ResponseEntity.ok(donorService.respondToRequest(donorId, requestId, status));
    }
}
