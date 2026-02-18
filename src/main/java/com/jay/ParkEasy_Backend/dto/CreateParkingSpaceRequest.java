package com.jay.ParkEasy_Backend.dto;

import com.jay.ParkEasy_Backend.entity.AmenityType;
import com.jay.ParkEasy_Backend.entity.AvailableDay;
import com.jay.ParkEasy_Backend.entity.ParkingType;
import com.jay.ParkEasy_Backend.entity.VehicleType;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class CreateParkingSpaceRequest {
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

    private Set<String> amenities;
    private Set<String> availableDays;

    private List<String> imageKeys;

}
