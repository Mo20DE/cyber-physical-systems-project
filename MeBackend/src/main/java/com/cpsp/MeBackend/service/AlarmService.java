package com.cpsp.MeBackend.service;

import com.cpsp.MeBackend.model.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.Calendar;

@Service
public class AlarmService {

  private final Logger logger = LoggerFactory.getLogger(AlarmService.class);

   @Autowired MeState meState;

  private int[] alarm;

  @Scheduled(cron = "0 */1 * * * *")
  public void checkForAlarm() {
    synchronized (this) {
      if (alarm != null
          && alarm.length == 2
          && alarm[0] == Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
          && alarm[1] == Calendar.getInstance().get(Calendar.MINUTE)){
        logger.info("The alarm is starting");
        meState.setEvent(Event.ALARM.value);
        deleteAlarm();
      }
    }
  }

  public void deleteAlarm() {
    synchronized (this) {
      alarm = null;
    }
  }

  public void setAlarm(int hour, int minute) {
    synchronized (this) {
      alarm = new int[] {hour, minute};
    }
  }
}
