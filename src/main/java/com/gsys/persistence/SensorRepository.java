package com.gsys.persistence;

import com.gsys.persistence.model.Sensor;
import org.springframework.data.repository.CrudRepository;

public interface SensorRepository extends CrudRepository<Sensor, String> {

}
