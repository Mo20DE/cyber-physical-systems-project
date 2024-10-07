package com.cpsp.MeBackend.service;

import com.cpsp.MeBackend.entity.SensorData;
import com.cpsp.MeBackend.model.CommandPaket;
import org.springframework.stereotype.Service;

@Service
public class CommandService {

  private final PaketService paketService;

  public CommandService(PaketService paketService) {
    this.paketService = paketService;
  }


  /**
   * to retrieve a command from sensor data
   * @param sensorData the input data
   * @return the command to send for the sensor data. May be null if no action is to be taken for the data
   */
  @Deprecated
  public byte[] getCommand(SensorData sensorData) {

    if (sensorData.getTemperature() >= 18
        && sensorData.getTemperature() <= 22
        && sensorData.getHumidity() >= 60) {
      return CommandPaket.MOOD_HAPPY;
    }

    if (sensorData.getTemperature() < 18) {
      return CommandPaket.MOOD_FREEZE;
    }

    if (sensorData.getTemperature() > 22) {
      return CommandPaket.MOOD_SWEATING;
    }

    return null;
  }


  /**
   * to map a command in the web interface to a command for the arduino
   * @param id the unique id
   * @return the corresponding command package
   */
  @Deprecated
  public byte[] getSimulatedCommand(int id) {
    switch (id) {
      case 0:
        return CommandPaket.MOOD_HAPPY;
      case 1:
        return CommandPaket.MOOD_FREEZE;
      case 2:
        return CommandPaket.MOOD_SWEATING;
      case 3:
        return CommandPaket.MOOD_TIRED;
    }
    return null;
  }
}
