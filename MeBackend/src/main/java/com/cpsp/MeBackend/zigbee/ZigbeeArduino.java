package com.cpsp.MeBackend.zigbee;

import com.digi.xbee.api.RemoteRaw802Device;
import com.digi.xbee.api.models.XBee64BitAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ZigbeeArduino {

    private final RemoteRaw802Device arduinoDevice;

    @Autowired
    public ZigbeeArduino(ZigbeePi zigbeePi, @Value("${arduino.xbee.address}") String arduinoAddress) {
        arduinoDevice = new RemoteRaw802Device(zigbeePi.getPiDevice(), new XBee64BitAddress(arduinoAddress));
    }

    public RemoteRaw802Device getArduinoDevice() {
        return arduinoDevice;
    }
}
