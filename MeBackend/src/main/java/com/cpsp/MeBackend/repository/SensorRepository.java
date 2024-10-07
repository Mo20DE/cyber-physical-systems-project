package com.cpsp.MeBackend.repository;

import com.cpsp.MeBackend.entity.SensorData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorRepository extends JpaRepository<SensorData, Long> {
}
