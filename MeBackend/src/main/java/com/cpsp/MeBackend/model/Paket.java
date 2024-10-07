package com.cpsp.MeBackend.model;

public enum Paket {
    HEADER_LENGTH(5),
    ACK_MESSAGE_ID(0),
    ERROR_MESSAGE_ID(1),
    INFORMATION_MESSAGE_ID(10),
    INFORMATION_MESSAGE_LENGTH(21),
    BODY_COMMAND_LENGTH(3),
    COMMAND_ID(20);

    public final int value;

    Paket(int value) {
        this.value = value;
    }
}
