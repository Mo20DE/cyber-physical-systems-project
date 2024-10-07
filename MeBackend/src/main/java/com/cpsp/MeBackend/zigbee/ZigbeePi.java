package com.cpsp.MeBackend.zigbee;

import com.digi.xbee.api.Raw802Device;
import com.digi.xbee.api.exceptions.XBeeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class ZigbeePi {

    @Autowired
    private final ApplicationContext applicationContext;

    private Raw802Device piDevice;

    public ZigbeePi(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        int portLimit = 10;
        for (int portNumber = 0; portNumber < portLimit; portNumber++) {
            String port = "";
            try {
                port = "/dev/ttyUSB" + Integer.toString(portNumber);
                piDevice = new Raw802Device(port, 9600);
                piDevice.open();
                System.out.println("Zigbee device is open ...");
                break;
            } catch (XBeeException e) {
                System.out.println("Port " + port + " is not available. Trying next ...");
            }
        }
//        if (!piDevice.isOpen()) {
//            System.out.println("NO ZIGBEE DEVICE IS CONNECTED, SHUTTING DOWN APPLICATION ...");
//            int exitCode = SpringApplication.exit(this.applicationContext, () -> 0);
//            System.exit(exitCode);
//        }
    }

    public Raw802Device getPiDevice() {
        return piDevice;
    }
}
