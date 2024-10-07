package com.cpsp.MeBackend.model;

public enum Event {
    NOTHING(0),
    ALARM(1),
    MONOPOLY_IMAGE(2),
    ALPACA_IMAGE(3),
    PANDA_IMAGE(4),
    PINK_PANTHER_SONG(5),
    STAR_WARS_SONG(6);

    public final int value;

    Event(int value) {
        this.value = value;
    }
}
