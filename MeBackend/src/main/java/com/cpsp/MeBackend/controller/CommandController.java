package com.cpsp.MeBackend.controller;

import com.cpsp.MeBackend.model.Driving;
import com.cpsp.MeBackend.model.Event;
import com.cpsp.MeBackend.model.Mood;
import com.cpsp.MeBackend.service.AlarmService;
import com.cpsp.MeBackend.service.CommandService;
import com.cpsp.MeBackend.service.MeState;
import com.cpsp.MeBackend.zigbee.ZigbeeSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class CommandController {

    private final CommandService commandService;
    private final ZigbeeSender zigbeeSender;

    private final AlarmService alarmService;

    private final MeState meState;

    private final Logger logger = LoggerFactory.getLogger(CommandController.class);

    private final static String OK_STATUS = "OK";
    private final static String ERROR_STATUS = "ERROR";


    public CommandController(CommandService commandService, ZigbeeSender zigbeeSender, AlarmService alarmService, MeState meState) {
        this.commandService = commandService;
        this.zigbeeSender = zigbeeSender;
        this.alarmService = alarmService;
        this.meState = meState;
    }

    @Deprecated
    @PostMapping("/command/{id}")
    @ResponseBody
    public String submitSimulatedCommand(@PathVariable Integer id) {
        logger.info("Received command request for id: " + id);
        meState.setEvent(id.byteValue());
        logger.error("Sending simulated command ...");
        return "OK";
    }

    @PostMapping("/alarm/set/{hour}-{minute}")
    @ResponseBody
    public String setAlarm(@PathVariable Integer hour, @PathVariable Integer minute){
        if(hour <= 24 && hour >= 0 && minute < 60 && minute >= 0){
            alarmService.setAlarm(hour, minute);
            logger.info("Alarm set by frontend for " + hour + ":" + minute);
            return OK_STATUS;
        }else{
            logger.warn("The frontend attempted to set an alarm for a invalid time: " + hour + ":" + minute);
            return ERROR_STATUS;
        }
    }

    @PostMapping("/alarm/deactivate")
    @ResponseBody
    public String deactivateAlarm(){
        alarmService.deleteAlarm();
        return OK_STATUS;
    }

    @PostMapping("/available")
    @ResponseBody
    public String meAvailable(){
        //logger.info("Received a request if the ME is available. The answer is: " + zigbeeSender.meAvailable);
        return String.valueOf(zigbeeSender.meAvailable);
    }

    /**
     * api for controlling the mood
     * @param id the id of the mood
     * @return the message returned to the sender of the request
     */
    @PostMapping("/mood/{id}")
    @ResponseBody
    public String submitSimulatedMood(@PathVariable Integer id){
        Optional<Mood> mood = Arrays.stream(Mood.values()).filter(d -> d.value == id).findFirst();
        if (mood.isEmpty()) {
            logger.warn("No valid mood command mapped to given id: " + id);
            return ERROR_STATUS;
        }
        logger.info("Sending mood command: " + mood.get().name());
        meState.setMood(id);
        return OK_STATUS;
    }


    /**
     * api for controlling events
     * @param id the id of the event
     * @return the message returned to the sender of the request
     */
    @PostMapping("/event/{id}")
    @ResponseBody
    public String submitSimulatedEvent(@PathVariable Integer id){
        Optional<Event> event = Arrays.stream(Event.values()).filter(d -> d.value == id).findFirst();
        if (event.isEmpty()) {
            logger.warn("No valid event command mapped to given id: " + id);
            return ERROR_STATUS;
        }
        logger.info("Sending event command: " + event.get().name());
        meState.setEvent(id);
        return OK_STATUS;
    }

    /**
     * api for controlling the driving
     * @param id the id of the movement
     * @return the message returned to the sender of the request
     */
    @PostMapping("/driving/{id}")
    @ResponseBody
    public String submitSimulatedMovement(@PathVariable Integer id){
        Optional<Driving> drivingDirection = Arrays.stream(Driving.values()).filter(d -> d.value == id).findFirst();
        if (drivingDirection.isEmpty()) {
            logger.warn("No valid driving command mapped to given id: " + id);
            return ERROR_STATUS;
        }
        logger.info("Sending driving command: " + drivingDirection.get().name());
        meState.setMoving(id);
        return OK_STATUS;
    }


}
