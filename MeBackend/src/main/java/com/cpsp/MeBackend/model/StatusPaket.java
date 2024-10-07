package com.cpsp.MeBackend.model;

public class StatusPaket {
    public static final byte[] ACK = {(byte) Paket.ACK_MESSAGE_ID.value, 0,0,0,0};
    public static final byte[] ERROR = {(byte) Paket.ERROR_MESSAGE_ID.value, (byte)Paket.ERROR_MESSAGE_ID.value ,0,0,0};
}
