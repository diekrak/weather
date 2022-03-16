package com.gsys.persistence;

import com.gsys.persistence.model.SensorReading;
import org.springframework.data.repository.CrudRepository;

public interface SensorReadingRepository  extends CrudRepository<SensorReading, Long> {
}
