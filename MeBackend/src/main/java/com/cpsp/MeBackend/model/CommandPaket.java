package com.cpsp.MeBackend.model;

public class CommandPaket {
    public static byte[] MOOD_HAPPY = {(byte) Paket.COMMAND_ID.value, 0, 0, 0, (byte) Mood.HAPPY.value};

    public static byte[] MOOD_FREEZE = {(byte) Paket.COMMAND_ID.value, 0, 0, 0, (byte) Mood.FREEZE.value};

    public static byte[] MOOD_TIRED = {(byte) Paket.COMMAND_ID.value, 0, 0, 0, (byte) Mood.TIRED.value};

    public static byte[] MOOD_SWEATING = {(byte) Paket.COMMAND_ID.value, 0, 0, 0, (byte) Mood.SWEATING.value};
}
