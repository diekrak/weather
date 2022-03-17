package com.gsys.services;

import com.gsys.persistence.SensorRepository;
import com.gsys.persistence.model.Sensor;
import com.gsys.services.json.GenericResponse;
import com.gsys.services.json.SensorRequest;
import com.gsys.util.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/sensor")
public class SensorController {

    @Autowired
    SensorRepository sensorRepository;


    /**
     * Registers a new sensor if it has not been register already
      * @param sensorRequest
     * @return
     */
    @PostMapping("/register")
    public ResponseEntity<GenericResponse> registerSensor(@RequestBody SensorRequest sensorRequest){
        if (sensorRepository.findById(sensorRequest.getId()).isPresent() ){
            return ResponseEntity.status(201).body(new GenericResponse(MessageResponse.SENSOR_ALREADY_REGISTERED));
        }
        sensorRepository.save(new Sensor(sensorRequest.getId(),sensorRequest.getCity(),sensorRequest.getCountry(),true, LocalDate.now(), LocalDate.now()));
        return ResponseEntity.ok().body(new GenericResponse(MessageResponse.SENSOR_ADDED ));
    }

    /**
     * Inactivates a sensor
     * @param id
     * @return
     */
    @PutMapping("/deactivate/{id}")
    public ResponseEntity<GenericResponse> deactivateSensor( @PathVariable String id){
        Optional<Sensor> sensorOptional = sensorRepository.findById(id);
        if (sensorOptional.isPresent() ){
            Sensor sensor = sensorOptional.get();
            sensor.setActive(false);
            sensor.setDateUpdate( LocalDate.now());
            sensorRepository.save(sensor);
            return ResponseEntity.ok().body(new GenericResponse(MessageResponse.SENSOR_INACTIVE));
        }
        return ResponseEntity.status(201).body(new GenericResponse(MessageResponse.SENSOR_NOT_EXIST));
    }

    /**
     * Aactivates a sensor
     * @param id
     * @return
     */
    @PutMapping("/activate/{id}")
    public ResponseEntity<GenericResponse> activateSensor( @PathVariable String id){
        Optional<Sensor> sensorOptional = sensorRepository.findById(id);
        if (sensorOptional.isPresent() ){
            Sensor sensor = sensorOptional.get();
            sensor.setActive(true);
            sensor.setDateUpdate( LocalDate.now());
            sensorRepository.save(sensor);
            return ResponseEntity.ok().body(new GenericResponse(MessageResponse.SENSOR_ACTIVE));
        }
        return ResponseEntity.status(201).body(new GenericResponse(MessageResponse.SENSOR_NOT_EXIST));
    }


}
