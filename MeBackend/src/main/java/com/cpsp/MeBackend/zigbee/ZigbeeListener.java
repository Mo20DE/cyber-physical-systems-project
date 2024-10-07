package com.cpsp.MeBackend.zigbee;

import com.cpsp.MeBackend.entity.SensorData;
import com.cpsp.MeBackend.model.Paket;
import com.cpsp.MeBackend.repository.SensorRepository;
import com.cpsp.MeBackend.service.CommandService;
import com.cpsp.MeBackend.service.MeState;
import com.cpsp.MeBackend.service.PaketService;
import com.digi.xbee.api.listeners.IDataReceiveListener;
import com.digi.xbee.api.models.XBeeMessage;
import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Queue;

@Service
public class ZigbeeListener implements IDataReceiveListener {

  private final PaketService paketService;

  private final ZigbeeSender zigbeeSender;

  private final MeState meState;

  private final SensorRepository sensorRepository;

  private final Logger logger = LoggerFactory.getLogger(ZigbeeListener.class);

  private final Queue<Byte> duplicateDetectionBuffer = new CircularFifoQueue<>(3);

  @Autowired
  public ZigbeeListener(
      ZigbeePi zigbeePi,
      PaketService paketService,
      MeState meState,
      SensorRepository sensorRepository,
      ZigbeeSender zigbeeSender) {
    this.meState = meState;
    this.sensorRepository = sensorRepository;
    zigbeePi.getPiDevice().addDataListener(this);
    this.paketService = paketService;
    this.zigbeeSender = zigbeeSender;
    logger.info("Listening ...");
  }

  /**
   * is called whenever a zigbee message is received. Has functionality to differentiate between
   * Acknowledgements, error messages and sensor data and makes simple error checking.
   *
   * @param xbeeMessage An {@code XBeeMessage} object containing the data, the {@code
   *     RemoteXBeeDevice} that sent the data and a flag indicating whether the data was sent via
   *     broadcast or not.
   */
  @Override
  public void dataReceived(XBeeMessage xbeeMessage) {
    logger.info("********* RECEIVED DATA *********");
    byte[] messageData = xbeeMessage.getData();

    Integer packetType = paketService.getMessageID(messageData);
    byte messageCode = paketService.extractMessageCode(messageData);

    // check checksum
    if (!paketService.checksumCorrect(messageData)) {
      logger.warn("The message was corrupted.");
      zigbeeSender.sendErrorCode(messageCode);
    }

    if (packetType.equals(Paket.ACK_MESSAGE_ID.value)) {
      zigbeeSender.receivedAck(messageCode);

    } else if (packetType.equals(Paket.ERROR_MESSAGE_ID.value)) {
      logger.warn("There was an error with the transmission");
      zigbeeSender.sendErrorCode(messageCode);

    } else if (packetType.equals(Paket.INFORMATION_MESSAGE_ID.value)) {
      handleSensorData(messageData, messageCode);

    } else {
      logger.error(
          "Something went wrong entirely. The paket was not corrupt but the message id "
              + packetType
              + " was not recognized!");
    }
  }

  /**
   * handles the message for proper sensor data
   *
   * @param message the message of the sensor data
   * @param messageCode the message code
   */
  private void handleSensorData(byte[] message, byte messageCode) {
    SensorData sensorData = paketService.parsePaket(message);
    if (sensorData == null) {
      logger.error("Error while parsing sensor data");
      zigbeeSender.sendErrorCode(messageCode);
      return;
    }
    // check if the sensor data is a duplicate
    if (!duplicateDetectionBuffer.contains(messageCode)) {
      duplicateDetectionBuffer.add(messageCode);
      //logger.info("PARSED SENSOR DATA: " + sensorData);
      zigbeeSender.sendACKCode(messageCode);
      sensorRepository.save(sensorData);
      //logger.info("Saved received sensor data in DB.");
      meState.updateState(sensorData);
    } else {
      zigbeeSender.sendACKCode(messageCode);
      logger.info("Duplicate Sensor data received.");
    }
  }
}
