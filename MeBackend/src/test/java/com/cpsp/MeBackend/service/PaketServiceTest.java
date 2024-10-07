package com.cpsp.MeBackend.service;

import com.cpsp.MeBackend.entity.SensorData;
import com.cpsp.MeBackend.model.Paket;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class PaketServiceTest {

    @Test
    void parsePaket() {
        PaketService paketService = new PaketService();
//        byte[] ts = {0x0, 0x40};
//        ByteBuffer byteBuffer = ByteBuffer.allocate(2);
//        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
//        byteBuffer.put(ts);
//        short s = byteBuffer.getShort();
//        System.out.println(s);
        short timestamp = 201;
        byte[] paket = {
                (byte) Paket.INFORMATION_MESSAGE_ID.value, 0, 0, 0, // HEADER
                (byte) (timestamp & 0xff), (byte) (timestamp & 0xff00 >> 8), // timestamp
                0, // button pressed
                0, 0, 0, 0, // temperature
                0, 0, 0, 0, // humidity
                0, 0, // distance
                0, 0, // visible light
                0, 0, // infrared light
                0, 0, 0, 0}; // ulraviolet light
        SensorData sensorData = paketService.parsePaket(paket);
        System.out.println(sensorData);
    }
}