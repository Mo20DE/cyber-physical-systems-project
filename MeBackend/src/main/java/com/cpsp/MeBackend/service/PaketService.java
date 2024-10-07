package com.cpsp.MeBackend.service;

import com.cpsp.MeBackend.model.Paket;
import com.cpsp.MeBackend.entity.SensorData;
import com.cpsp.MeBackend.model.StatusPaket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;

@Service
public class PaketService {

  private final Logger logger = LoggerFactory.getLogger(PaketService.class);

  @Autowired
  public PaketService() {}

  byte messageCode = 0;

  public SensorData parsePaket(byte[] messageData) {
    // HEADER
    if (messageData.length < Paket.HEADER_LENGTH.value) {
      logger.error("Paket is smaller than defined header size");
      return null;
    }
    byte messageId = messageData[0];

    Timestamp timestamp = Timestamp.from(Instant.now());
    long duration = getShort(messageData, 3) * 1000;
    //logger.info("The data is " + duration / 1000 + " second(s) old");
    timestamp.setTime(timestamp.getTime() - duration);

    // BODY
    int bytesRead = 5;
    if (messageId == Paket.INFORMATION_MESSAGE_ID.value) {
      if (messageData.length
          != Paket.INFORMATION_MESSAGE_LENGTH.value + Paket.HEADER_LENGTH.value) {
        logger.error("Invalid body length for info message");
        return null;
      }
      short loop = getShort(messageData, bytesRead);
      bytesRead += 2;
      boolean buttonPressed = messageData[bytesRead] != 0;
      bytesRead += 1;
      float temperature = getFloat(messageData, bytesRead);
      bytesRead += 4;
      float humidity = getFloat(messageData, bytesRead);
      bytesRead += 4;
      short distance = getShort(messageData, bytesRead);
      bytesRead += 2;
      short visibleLight = getShort(messageData, bytesRead);
      bytesRead += 2;
      short infraredLight = getShort(messageData, bytesRead);
      bytesRead += 2;
      float ultravioletLight = getFloat(messageData, bytesRead);
      return new SensorData(
          timestamp,
          buttonPressed,
          temperature,
          humidity,
          distance,
          visibleLight,
          infraredLight,
          ultravioletLight);
    } else {
      logger.error("Unknown message ID: " + messageId);
    }
    return null;
  }

  public int getMessageID(byte[] paket) {
    if (paket.length < 5) {
      logger.warn("received paket was too short");
      return Paket.ERROR_MESSAGE_ID.value;
    }else{
      return paket[0];
    }
  }

  public boolean checksumCorrect(byte[] paket){
    // check checksum
    byte paketChecksum = paket[1];
    byte calculatedChecksum = getChecksum(paket);

    if(paketChecksum == calculatedChecksum){
      return true;
    }else {
      logger.info(
              "the checksums do not match. The calculated was "
                      + calculatedChecksum
                      + " and the received was "
                      + paketChecksum);
      return false;
    }
  }

  public byte extractMessageCode(byte[] packet) {
    return packet[2];
  }

  // ****************
  // utility methods
  // ****************

  public synchronized byte getMessageCode() {
    messageCode++;
    return (byte) (messageCode - 1);
  }


  public byte[] getAck(byte messageCode) {
    byte[] paket = StatusPaket.ACK;
    paket[2] = messageCode;
    paket[1] = getChecksum(paket);
    return paket;
  }

  public byte[] getErr(byte messageCode) {
    byte[] paket = StatusPaket.ERROR;
    paket[2] = messageCode;
    paket[1] = getChecksum(paket);
    return paket;
  }

  public synchronized byte[] injectMessageCode(byte[] input) {
    input[2] = getMessageCode();
    return input;
  }

  public static float getFloat(byte[] input, int index) {
    if (input == null || input.length < 4) {
      throw new IllegalArgumentException(
          "Not enough bytes given to make a valid short conversion.");
    }
    if (index > input.length - 4) {
      throw new IllegalArgumentException("The given index is too high to make a valid conversion");
    }
    return ByteBuffer.wrap(input).order(ByteOrder.LITTLE_ENDIAN).getFloat(index);
  }

  public static short getShort(byte[] input, int index) {
    if (input == null || input.length < 2) {
      throw new IllegalArgumentException(
          "Not enough bytes given to make a valid short conversion.");
    }
    if (index > input.length - 2) {
      throw new IllegalArgumentException("The given index is too high to make a valid conversion");
    }
    return ByteBuffer.wrap(input).order(ByteOrder.LITTLE_ENDIAN).getShort(index);
  }

  public void fillChecksum(byte[] paket) {
    byte checksum = getChecksum(paket);
    paket[1] = checksum;
  }

  public byte getChecksum(byte[] input) {
    byte setChecksum = input[1];
    input[1] = 0;
    byte sum = 0;
    for (byte b : input) {
      sum += b;
    }
    input[1] = setChecksum;
    //logger.info("Checksum for: " + Arrays.toString(input) + " is " + sum);
    return sum;
  }

  public byte[] getCommandsFromState(byte mood, byte moving, byte event){
    byte[] packet = new byte[Paket.HEADER_LENGTH.value + Paket.BODY_COMMAND_LENGTH.value];
    packet[0] = 20;
    packet[5] = mood;
    packet[6] = moving;
    packet[7] = event;
    packet[2] = getMessageCode();
    packet[1] = getChecksum(packet);
    return packet;
  }

}
