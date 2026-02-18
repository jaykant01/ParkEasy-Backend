package com.jay.ParkEasy_Backend.controller;

import com.jay.ParkEasy_Backend.dto.CreateParkingSpaceRequest;
import com.jay.ParkEasy_Backend.dto.ParkingSpaceResponse;
import com.jay.ParkEasy_Backend.security.CustomUserDetails;
import com.jay.ParkEasy_Backend.service.ParkingSpaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/parking")
@RequiredArgsConstructor
public class ParkingSpaceController {

    private final ParkingSpaceService parkingSpaceService;

    // Upload images
    @PostMapping(
            value = "/upload-images",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<List<String>> uploadImages(
            @RequestPart("images")
            MultipartFile[] images,
            @AuthenticationPrincipal CustomUserDetails user
    ) {

        List<String> keys =
                parkingSpaceService.uploadImages(
                        images,
                        user.getId()
                );

        return ResponseEntity.ok(keys);
    }

    // Create parking
    @PostMapping("/create")
    public ResponseEntity<ParkingSpaceResponse> createSpace(
            @RequestBody CreateParkingSpaceRequest request,
            @AuthenticationPrincipal CustomUserDetails user
    ) {

        ParkingSpaceResponse resp =
                parkingSpaceService.createSpace(
                        request,
                        user.getId()
                );

        return ResponseEntity.ok(resp);
    }

    // Get parking spaces
    @GetMapping("/my-spaces")
    public ResponseEntity<List<ParkingSpaceResponse>> getMySpaces(
            @AuthenticationPrincipal CustomUserDetails user
    ) {

        List<ParkingSpaceResponse> spaces =
                parkingSpaceService.getSpacesByUser(user.getId());

        return ResponseEntity.ok(spaces);
    }

    // Get by id
    @GetMapping("/{id}")
    public ResponseEntity<ParkingSpaceResponse> getById(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails user
    ) {

        ParkingSpaceResponse space =
                parkingSpaceService.getById(id, user.getId());

        return ResponseEntity.ok(space);
    }

    // Update parking space
    @PutMapping("/edit-space/{id}")
    public ResponseEntity<ParkingSpaceResponse> updateSpace(
            @PathVariable Long id,
            @RequestBody CreateParkingSpaceRequest request,
            @AuthenticationPrincipal CustomUserDetails user
    ) {

        ParkingSpaceResponse updated =
                parkingSpaceService.updateSpace(
                        id,
                        request,
                        user.getId()
                );

        return ResponseEntity.ok(updated);
    }

    // Delete My Space
    @DeleteMapping("/delete-space/{id}")
    public ResponseEntity<Void> deleteSpace(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails user
    ) {

        parkingSpaceService.deleteSpace(id, user.getId());

        return ResponseEntity.ok().build();
    }


    // Search Nearby parking space
    @GetMapping("/nearby")
    public ResponseEntity<List<ParkingSpaceResponse>> getNearbySpaces(
            @RequestParam double lat,
            @RequestParam double lng,
            @RequestParam(defaultValue = "3") double radiusKm
    ) {

        List<ParkingSpaceResponse> spaces =
                parkingSpaceService.findNearby(lat, lng, radiusKm);

        return ResponseEntity.ok(spaces);
    }

}
