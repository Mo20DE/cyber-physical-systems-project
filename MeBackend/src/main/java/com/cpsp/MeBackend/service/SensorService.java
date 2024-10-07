package com.cpsp.MeBackend.service;

import com.cpsp.MeBackend.entity.SensorData;
import com.cpsp.MeBackend.repository.SensorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SensorService {

    private final SensorRepository sensorRepository;

    @Autowired
    public SensorService(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    public List<SensorData> getAllSensorData() {
        return sensorRepository.findAll();
    }
}
