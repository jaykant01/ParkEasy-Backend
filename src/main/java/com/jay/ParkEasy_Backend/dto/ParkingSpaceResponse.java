package com.jay.ParkEasy_Backend.dto;

import com.jay.ParkEasy_Backend.entity.*;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
public class ParkingSpaceResponse {
    private Long id;
    private String title;
    private String address;
    private String city;
    private String zip;
    private Double latitude;
    private Double longitude;
    private ParkingType parkingType;
    private VehicleType vehicleType;
    private Integer pricePerHour;
    private Integer totalSpaces;
    private String description;
    private Set<AmenityType> amenities;
    private Set<AvailableDay> availableDays;
    private List<String> imageUrls;
    private ParkingStatus status;
    private UUID userId;


    public static ParkingSpaceResponse fromEntity(ParkingSpaceEntity e, List<String> imageUrls) {
        return ParkingSpaceResponse.builder()
                .id(e.getId())
                .title(e.getTitle())
                .address(e.getAddress())
                .city(e.getCity())
                .zip(e.getZip())
                .latitude(e.getLatitude())
                .longitude(e.getLongitude())
                .parkingType(e.getParkingType())
                .vehicleType(e.getVehicleType())
                .pricePerHour(e.getPricePerHour())
                .totalSpaces(e.getTotalSpaces())
                .description(e.getDescription())
                .amenities(e.getAmenities())
                .availableDays(e.getAvailableDays())
                .imageUrls(imageUrls)
                .status(e.getStatus())
                .userId(e.getUserId())
                .build();
    }



}
