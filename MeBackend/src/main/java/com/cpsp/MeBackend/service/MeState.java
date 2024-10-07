package com.cpsp.MeBackend.service;

import com.cpsp.MeBackend.entity.SensorData;
import com.cpsp.MeBackend.model.Driving;
import com.cpsp.MeBackend.model.Event;
import com.cpsp.MeBackend.model.Mood;
import com.cpsp.MeBackend.zigbee.ZigbeeSender;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Queue;

@Service
public class MeState {

  @Autowired PaketService paketService;

  private final Logger logger = LoggerFactory.getLogger(ZigbeeSender.class);

  byte mood;
  Queue<Byte> moving = new LinkedList<>();
  Queue<Byte> event = new LinkedList<>();

  @Setter @Getter boolean changeToSend = false;

  public synchronized void setMood(int mood) {
    if(this.mood != (byte) mood){
      logger.info("Mood changed, old: " + this.mood + ", new: " + mood);
      changeToSend = true;
      this.mood = (byte) mood;
    }
  }

  public synchronized void setMoving(int moving) {
    if (moving == Driving.NOTHING.value){
      return;
    }
    this.moving.add((byte) moving);
    changeToSend = true;
  }

  public synchronized void setEvent(int event) {
    if (event == Event.NOTHING.value){
      return;
    }
    if (!this.moving.contains((byte) event)) {
      this.event.add((byte) event);
      changeToSend= true;
    }
  }

  public synchronized byte[] getPacket() {
    byte moving;
    byte event;

    if (this.moving.size() == 0) {
      moving = (byte) Driving.NOTHING.value;
    } else {
      moving = this.moving.poll();
    }

    if (this.event.size() == 0) {
      event = (byte) Event.NOTHING.value;
    } else {
      event = this.event.poll();
    }

    if(this.event.isEmpty() && this.moving.isEmpty()){
      changeToSend = false;
    }

    return paketService.getCommandsFromState(mood, moving, event);
  }

  public void updateState(SensorData sensorData) {
    if (sensorData.getTemperature() >= 18
        && sensorData.getTemperature() <= 22
        && sensorData.getHumidity() >= 60) {
      setMood(Mood.HAPPY.value);
      return;
    }

    if (sensorData.getTemperature() < 18) {
      setMood(Mood.FREEZE.value);
      return;
    }

    if (sensorData.getTemperature() > 22) {
      setMood(Mood.SWEATING.value);
      return;
    }

    // it
    if (sensorData.getVisibleLight() < 200) {
      setMood(Mood.SLEEPING.value);
    }
  }
}
