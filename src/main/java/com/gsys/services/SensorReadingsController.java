package com.gsys.services;

import com.gsys.persistence.MetricRepository;
import com.gsys.persistence.SensorReadingRepository;
import com.gsys.persistence.SensorRepository;
import com.gsys.persistence.model.Metric;
import com.gsys.persistence.model.Sensor;
import com.gsys.persistence.model.SensorReading;
import com.gsys.services.jsonMessage.GenericResponse;
import com.gsys.util.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/sensor_readings")
public class SensorReadingsController {

    @Autowired
    SensorReadingRepository sensorReadingRepository;

    @Autowired
    SensorRepository sensorRepository;

    @Autowired
    MetricRepository metricRepository;

    @PostMapping("/add")
    public ResponseEntity<GenericResponse> registerSensor(@RequestBody SensorReading sensorReading){
        Optional<Sensor> sensorOptional = sensorRepository.findById(sensorReading.getSensorId());
        if (sensorOptional.isEmpty()) {
            return ResponseEntity.status(201).body(new GenericResponse(MessageResponse.SENSOR_NOT_EXIST));
        }
        if (!sensorOptional.get().getActive()) {
            return ResponseEntity.status(201).body(new GenericResponse(MessageResponse.SENSOR_INACTIVE));
        }

        Optional<Metric> metricOptional = metricRepository.findById(sensorReading.getMetricId());
        if (metricOptional.isEmpty()) {
            return ResponseEntity.status(201).body(new GenericResponse(MessageResponse.METRIC_NOT_EXIST));
        }

        sensorReadingRepository.save(sensorReading);
        return ResponseEntity.ok().body(new GenericResponse(MessageResponse.READING_ADDED));
    }

}
