package com.cpsp.MeBackend.zigbee;

import com.cpsp.MeBackend.service.MeState;
import com.cpsp.MeBackend.service.PaketService;
import com.digi.xbee.api.exceptions.XBeeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

/** sends commands to the arduino and handles their reply */
@Service
public class ZigbeeSender {

  private final ZigbeePi zigbeePi;

  private final ZigbeeArduino zigbeeArduino;

  private final PaketService paketService;

  private final MeState meState;

  public boolean meAvailable = false;

  public int numTransmits = 0;

  public int previousMessageCode;

  private boolean awaitingCommandACK = false;

  private byte[] lastMessage = null;

  private final Logger logger = LoggerFactory.getLogger(ZigbeeSender.class);

  @Autowired
  public ZigbeeSender(
      ZigbeePi zigbeePi, ZigbeeArduino zigbeeArduino, PaketService paketService, MeState meState) {
    this.zigbeePi = zigbeePi;
    this.zigbeeArduino = zigbeeArduino;
    this.paketService = paketService;
    this.meState = meState;
  }

  /**
   * sends a packet via xbee
   *
   * @param paket the paket as byte[]
   */
  private synchronized void sendPaket(byte[] paket) {
    logger.info("Sending : " + Arrays.toString(paket));
    try {
      zigbeePi.getPiDevice().sendData(zigbeeArduino.getArduinoDevice(), paket);
    } catch (XBeeException e) {
      logger.error("Something went wrong while sending paket: " + e.getMessage());
    }
  }

  /**
   * the method to send the current state of the ME to the arduino, has a fixed schedule TODO:
   * determine best possible scheduling time
   */
  @Scheduled(fixedDelay = 500)
  private synchronized void scheduledSend() {
    if ((!awaitingCommandACK) && (!meState.isChangeToSend())) {
      return;
    }
    if (lastMessage == null) {
      lastMessage = meState.getPacket();
    }
    sendPaket(lastMessage);
    awaitingCommandACK = true;
    numTransmits++;
    if (numTransmits >= 4) {
      logger.warn("ME is not available, discarding command");
      discardOrAckPacket();
      meAvailable = false;
    }
  }

  private synchronized void discardOrAckPacket(){
    lastMessage = null;
    numTransmits = 0;
    awaitingCommandACK = false;
  }

  /**
   * sends an ack to the ME(arduino)
   *
   * @param messageCode the message code of the received sensor data package
   */
  public void sendACKCode(byte messageCode) {
    // logger.info("Sending ACK Paket ...");
    sendPaket(paketService.getAck(messageCode));
  }

  /**
   * sends an error message to the ME(arduino)
   *
   * @param messageCode the message code of the received sensor data package
   */
  public void sendErrorCode(byte messageCode) {
    // logger.info("Sending Error Paket ...");
    sendPaket(paketService.getErr(messageCode));
  }

  /**
   * to be called when an acknowledgement was received by the xbee module, is received
   *
   * @param messageCode the message code in the header of the ack
   */
  public synchronized void receivedAck(byte messageCode) {
    synchronized (this) {
      if (lastMessage == null) {
        logger.warn("Received an ack but it was not expected. It might be a duplicate");
        return;
      }
      if (messageCode == paketService.extractMessageCode(lastMessage)) {
        // logger.info("Valid Ack received for Message Code: " + messageCode);
        // previous message was acknowledged
        if (!meAvailable) {
          logger.info("ME is now available");
          meState.setChangeToSend(true);
        }
        discardOrAckPacket();
        meAvailable = true;
      } else {
        // the ack does not have the same message code
        logger.warn(
            "An ack arrived which was not expected. Expected: "
                + previousMessageCode
                + " Received: "
                + messageCode);
      }
    }
  }
}
