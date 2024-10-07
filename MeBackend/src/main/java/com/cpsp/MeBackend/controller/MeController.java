package com.cpsp.MeBackend.controller;

import com.cpsp.MeBackend.entity.SensorData;
import com.cpsp.MeBackend.service.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class MeController {

    private final SensorService sensorService;

    @Autowired
    public MeController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @GetMapping("/")
    public String showHomePage(Model model) {
        List<SensorData> sensorDataList = sensorService.getAllSensorData();
        Collections.reverse(sensorDataList);
        sensorDataList = sensorDataList.stream().limit(20).collect(Collectors.toList());
        model.addAttribute("sensorDataList", sensorDataList);
        return "homepage";
    }
}
