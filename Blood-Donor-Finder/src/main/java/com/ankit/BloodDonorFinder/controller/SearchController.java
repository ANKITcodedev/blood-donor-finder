package com.ankit.BloodDonorFinder.controller;

import com.ankit.BloodDonorFinder.model.DonorProfile;
import com.ankit.BloodDonorFinder.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
@Tag(name = "Search",
        description = "Find nearby blood donors using location")
public class SearchController {

    private final SearchService searchService;

    // find nearby donors by bloodgroup
    @Operation(
            summary = "Find nearby donors",
            description = "Find available donors near a location. " +
                    "Uses Haversine formula to calculate distance. " +
                    "Default radius is 10km. " +
                    "Blood groups: O_POSITIVE, O_NEGATIVE, A_POSITIVE, " +
                    "A_NEGATIVE, B_POSITIVE, B_NEGATIVE, AB_POSITIVE, AB_NEGATIVE"
    )
    @GetMapping("/donors")
    public ResponseEntity<List<DonorProfile>> findNearByDonors(
            @RequestParam String bloodGroup, @RequestParam Double latitude,
            @RequestParam Double longitude, @RequestParam(defaultValue = "10.0") Double radiusKm) {

        return ResponseEntity.ok(searchService.findNearByDonors(bloodGroup, latitude, longitude, radiusKm));
    }

    // find donor for a specific blood request
    @Operation(
            summary = "Find donors for request",
            description = "Find available donors near a specific blood request's hospital. " +
                    "Automatically uses hospital location from the request."
    )
    @GetMapping("/request/{requestId}")
    public ResponseEntity<List<DonorProfile>> findDonorsForRequest(
            @PathVariable Long requestId,
            @RequestParam(defaultValue = "10.0") Double radiusKm) {

        return ResponseEntity.ok(searchService.findDonorsForRequest(requestId, radiusKm));
    }
}
