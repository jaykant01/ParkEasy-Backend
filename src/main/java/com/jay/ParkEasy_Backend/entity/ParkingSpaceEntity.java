package com.jay.ParkEasy_Backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "parking_spaces")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParkingSpaceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Basic info
    private String title;
    private String address;
    private String city;
    private String zip;

    // Location
    private Double latitude;
    private Double longitude;

    // Pricing
    private Integer pricePerHour;
    private Integer totalSpaces;

    // Type
    @Enumerated(EnumType.STRING)
    private ParkingType parkingType;

    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;

    // Description
    @Column(columnDefinition = "TEXT")
    private String description;

    // Availability Days (LAZY)
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "parking_available_days",
            joinColumns = @JoinColumn(name = "parking_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "day")
    private Set<AvailableDay> availableDays;

    // Amenities (LAZY)
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "parking_amenities",
            joinColumns = @JoinColumn(name = "parking_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "amenity")
    private Set<AmenityType> amenities;

    // Store ONLY S3 keys
    @Builder.Default
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "parking_images",
            joinColumns = @JoinColumn(name = "parking_id")
    )
    @Column(name = "image_key", length = 512)
    private List<String> imageKeys = new java.util.ArrayList<>();

    // Owner
    private UUID userId;

    @Enumerated(EnumType.STRING)
    private ParkingStatus status = ParkingStatus.ACTIVE;

    private Long createdAt;
    private Long updatedAt;

    @PrePersist
    public void prePersist() {
        long now = System.currentTimeMillis();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = System.currentTimeMillis();

    }
}
