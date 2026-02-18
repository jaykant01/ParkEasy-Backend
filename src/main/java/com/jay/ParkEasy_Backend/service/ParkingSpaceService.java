package com.jay.ParkEasy_Backend.service;

import com.jay.ParkEasy_Backend.dto.CreateParkingSpaceRequest;
import com.jay.ParkEasy_Backend.dto.ParkingSpaceResponse;
import com.jay.ParkEasy_Backend.entity.AmenityType;
import com.jay.ParkEasy_Backend.entity.AvailableDay;
import com.jay.ParkEasy_Backend.entity.ParkingSpaceEntity;
import com.jay.ParkEasy_Backend.entity.ParkingStatus;
import com.jay.ParkEasy_Backend.repository.ParkingSpaceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ParkingSpaceService {

    private final ParkingSpaceRepository parkingSpaceRepository;
    private final S3UploadService s3UploadService;

    // Upload images
    public List<String> uploadImages(MultipartFile[] images, UUID userId) {
        return s3UploadService.uploadImages(images);
    }

    // Create parking
    @Transactional
    public ParkingSpaceResponse createSpace(
            CreateParkingSpaceRequest req,
            UUID userId) {

        Set<AmenityType> amenities = convertAmenities(req.getAmenities());
        Set<AvailableDay> availableDays = convertDays(req.getAvailableDays());

        ParkingSpaceEntity space = ParkingSpaceEntity.builder()
                .title(req.getTitle())
                .address(req.getAddress())
                .city(req.getCity())
                .zip(req.getZip())
                .latitude(req.getLatitude())
                .longitude(req.getLongitude())
                .parkingType(req.getParkingType())
                .vehicleType(req.getVehicleType())
                .pricePerHour(req.getPricePerHour())
                .totalSpaces(req.getTotalSpaces())
                .description(req.getDescription())
                .amenities(amenities)
                .availableDays(availableDays)
                .imageKeys(
                        req.getImageKeys() == null
                                ? List.of()
                                : req.getImageKeys()
                )
                .userId(userId)
                .status(ParkingStatus.ACTIVE)
                .build();

        ParkingSpaceEntity saved =
                parkingSpaceRepository.save(space);

        List<String> imageUrls =
                saved.getImageKeys()
                        .stream()
                        .map(s3UploadService::getImageUrl)
                        .toList();

        return ParkingSpaceResponse.fromEntity(saved, imageUrls);

    }

    // ================= ENUM HELPERS =================

    private Set<AmenityType> convertAmenities(Set<String> input) {

        if (input == null) return Set.of();

        return input.stream()
                .map(this::normalizeEnum)
                .map(name -> {
                    try {
                        return AmenityType.valueOf(name);
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    private Set<AvailableDay> convertDays(Set<String> input) {

        if (input == null) return Set.of();

        return input.stream()
                .map(day -> day.trim().toUpperCase())
                .map(name -> {
                    try {
                        return AvailableDay.valueOf(name);
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    private String normalizeEnum(String s) {

        if (s == null) return "";

        return s.trim()
                .toUpperCase()
                .replaceAll("[^A-Z0-9]+", "_");
    }

    public List<ParkingSpaceResponse> getSpacesByUser(UUID userId) {

        return parkingSpaceRepository
                .findByUserId(userId)
                .stream()
                .map(space -> {

                    List<String> imageUrls =
                            space.getImageKeys()
                                    .stream()
                                    .map(s3UploadService::getImageUrl)
                                    .toList();

                    return ParkingSpaceResponse
                            .fromEntity(space, imageUrls);
                })
                .toList();
    }

    public ParkingSpaceResponse getById(Long id, UUID userId) {

        ParkingSpaceEntity space =
                parkingSpaceRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Parking space not found")
                        );

        // SECURITY CHECK
        if (!space.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized access");
        }

        List<String> imageUrls =
                space.getImageKeys()
                        .stream()
                        .map(s3UploadService::getImageUrl)
                        .toList();

        return ParkingSpaceResponse.fromEntity(space, imageUrls);
    }


    @Transactional
    public ParkingSpaceResponse updateSpace(
            Long id,
            CreateParkingSpaceRequest req,
            UUID userId
    ) {

        ParkingSpaceEntity space =
                parkingSpaceRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Parking space not found")
                        );

        // SECURITY CHECK
        if (!space.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized update attempt");
        }

        // Update fields
        space.setTitle(req.getTitle());
        space.setAddress(req.getAddress());
        space.setCity(req.getCity());
        space.setZip(req.getZip());
        space.setLatitude(req.getLatitude());
        space.setLongitude(req.getLongitude());
        space.setParkingType(req.getParkingType());
        space.setVehicleType(req.getVehicleType());
        space.setPricePerHour(req.getPricePerHour());
        space.setTotalSpaces(req.getTotalSpaces());
        space.setDescription(req.getDescription());

        space.setAmenities(convertAmenities(req.getAmenities()));
        space.setAvailableDays(convertDays(req.getAvailableDays()));

        if (req.getImageKeys() != null) {
            space.setImageKeys(req.getImageKeys());
        }

        ParkingSpaceEntity updated =
                parkingSpaceRepository.save(space);

        List<String> imageUrls =
                updated.getImageKeys()
                        .stream()
                        .map(s3UploadService::getImageUrl)
                        .toList();

        return ParkingSpaceResponse.fromEntity(updated, imageUrls);
    }

    @Transactional
    public void deleteSpace(Long id, UUID userId) {

        ParkingSpaceEntity space =
                parkingSpaceRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Parking space not found")
                        );

        // SECURITY CHECK
        if (!space.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized delete attempt");
        }

        parkingSpaceRepository.delete(space);
    }


    // Search nearby parking space
    public List<ParkingSpaceResponse> findNearby(
            double userLat,
            double userLng,
            double radiusKm
    ) {

        List<ParkingSpaceEntity> all =
                parkingSpaceRepository.findAll();

        return all.stream()
                .filter(space ->
                        space.getLatitude() != null &&
                                space.getLongitude() != null &&
                                space.getStatus() == ParkingStatus.ACTIVE
                )
                .filter(space -> {

                    double distance = haversine(
                            userLat,
                            userLng,
                            space.getLatitude(),
                            space.getLongitude()
                    );

                    return distance <= radiusKm;
                })
                .map(space -> {

                    List<String> imageUrls =
                            space.getImageKeys()
                                    .stream()
                                    .map(s3UploadService::getImageUrl)
                                    .toList();

                    return ParkingSpaceResponse
                            .fromEntity(space, imageUrls);
                })
                .toList();
    }



    private double haversine(
            double lat1,
            double lon1,
            double lat2,
            double lon2
    ) {

        final int EARTH_RADIUS = 6371; // km

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a =
                Math.sin(dLat / 2) * Math.sin(dLat / 2)
                        +
                        Math.cos(Math.toRadians(lat1))
                                *
                                Math.cos(Math.toRadians(lat2))
                                *
                                Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(
                Math.sqrt(a),
                Math.sqrt(1 - a)
        );

        return EARTH_RADIUS * c;
    }


}