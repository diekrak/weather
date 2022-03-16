package com.gsys.services;

import com.gsys.persistence.SensorRepository;
import com.gsys.persistence.model.Sensor;
import com.gsys.services.jsonMessage.GenericResponse;
import com.gsys.services.jsonMessage.SensorRequest;
import com.gsys.util.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("/sensor")
public class SensorController {

    @Autowired
    SensorRepository sensorRepository;

    @PostMapping("/register")
    public ResponseEntity<GenericResponse> registerSensor(@RequestBody SensorRequest sensorRequest){
        if (sensorRepository.findById(sensorRequest.getId()).isPresent() ){
            return ResponseEntity.status(201).body(new GenericResponse(MessageResponse.SENSOR_EXIST));
        }
        Date now = new Date();
        sensorRepository.save(new Sensor(sensorRequest.getId(),sensorRequest.getCity(),sensorRequest.getCountry(),true, now, now));
        return ResponseEntity.ok().body(new GenericResponse(MessageResponse.SENSOR_ADDED ));
    }

    @PutMapping("/deactivate/{id}")
    public ResponseEntity<GenericResponse> deactivateSensor( @PathVariable String id){
        Optional<Sensor> sensorOptional = sensorRepository.findById(id);
        if (sensorOptional.isPresent() ){
            Sensor sensor = sensorOptional.get();
            sensor.setActive(false);
            sensor.setDateUpdate( new Date());
            sensorRepository.save(sensor);
            return ResponseEntity.status(201).body(new GenericResponse(1, "The Sensor is inactive"));
        }
        return ResponseEntity.ok().body(new GenericResponse(-1, "The Sensor does not exists"));
    }

    @PutMapping("/activate/{id}")
    public ResponseEntity<GenericResponse> activateSensor( @PathVariable String id){
        Optional<Sensor> sensorOptional = sensorRepository.findById(id);
        if (sensorOptional.isPresent() ){
            Sensor sensor = sensorOptional.get();
            sensor.setActive(true);
            sensor.setDateUpdate( new Date());
            sensorRepository.save(sensor);
            return ResponseEntity.status(201).body(new GenericResponse(1, "The Sensor is active"));
        }
        return ResponseEntity.ok().body(new GenericResponse(-1, "The Sensor does not exists"));
    }


}
