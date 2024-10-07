package com.cpsp.MeBackend.config;

import com.cpsp.MeBackend.entity.SensorData;
import com.cpsp.MeBackend.repository.SensorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class SensorConfig implements CommandLineRunner {

  private final SensorRepository sensorRepository;

  public SensorConfig(SensorRepository sensorRepository) {
    this.sensorRepository = sensorRepository;
  }

  @Override
  public void run(String... args) {
    /*
    ArrayList<SensorData> list = new ArrayList<>();
    for (int i = 0; i < 30; i++) {
      list.add(
          new SensorData(
              Timestamp.from(Instant.now()),
              false,
              20.0F + i,
              0.5F + i,
              (short) (300 + i),
              (short) (50 + i),
              (short) (50 + i),
              0.3F + i));
    }
    list.forEach(sensorRepository::save);
     */
  }
}
