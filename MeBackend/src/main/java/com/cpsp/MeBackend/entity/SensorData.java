package com.cpsp.MeBackend.entity;

import javax.persistence.*;
import java.sql.Timestamp;

@Table
@Entity
public class SensorData {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private Timestamp timestamp;
    private boolean buttonPressed;
    private float temperature;
    private float humidity;
    private short distance;
    private short visibleLight;
    private short infraredLight;
    private float ultravioletLight;

    public SensorData() {
    }

    public SensorData(Timestamp timestamp, boolean buttonPressed, float temperature, float humidity, short distance, short visibleLight, short infraredLight, float ultravioletLight) {
        this.timestamp = timestamp;
        this.buttonPressed = buttonPressed;
        this.temperature = temperature;
        this.humidity = humidity;
        this.distance = distance;
        this.visibleLight = visibleLight;
        this.infraredLight = infraredLight;
        this.ultravioletLight = ultravioletLight;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isButtonPressed() {
        return buttonPressed;
    }

    public void setButtonPressed(boolean buttonPressed) {
        this.buttonPressed = buttonPressed;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public float getHumidity() {
        return humidity;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }

    public short getDistance() {
        return distance;
    }

    public void setDistance(short distance) {
        this.distance = distance;
    }

    public short getVisibleLight() {
        return visibleLight;
    }

    public void setVisibleLight(short visibleLight) {
        this.visibleLight = visibleLight;
    }

    public short getInfraredLight() {
        return infraredLight;
    }

    public void setInfraredLight(short infraredLight) {
        this.infraredLight = infraredLight;
    }

    public float getUltravioletLight() {
        return ultravioletLight;
    }

    public void setUltravioletLight(float ultravioletLight) {
        this.ultravioletLight = ultravioletLight;
    }

    @Override
    public String toString() {
        return "SensorData{" +
                "timestamp=" + timestamp +
                ", buttonPressed=" + buttonPressed +
                ", temperature=" + temperature +
                ", humidity=" + humidity +
                ", distance=" + distance +
                ", visibleLight=" + visibleLight +
                ", infraredLight=" + infraredLight +
                ", ultravioletLight=" + ultravioletLight +
                '}';
    }
}
