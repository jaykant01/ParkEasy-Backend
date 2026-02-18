package com.jay.ParkEasy_Backend.repository;

import com.jay.ParkEasy_Backend.entity.ParkingSpaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ParkingSpaceRepository extends JpaRepository<ParkingSpaceEntity, Long> {
    List<ParkingSpaceEntity> findByUserId(UUID userId);
}
