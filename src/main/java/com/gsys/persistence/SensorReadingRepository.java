package com.gsys.persistence;

import com.gsys.persistence.model.SensorReading;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface SensorReadingRepository  extends CrudRepository<SensorReading, Long> {


    @Query(value = "select sensor_id,metric_id,avg(value) FROM sensor_reading WHERE metric_id in ?1 AND sensor_id in ?2 AND date BETWEEN ?3 AND ?4 GROUP BY sensor_id,metric_id;", nativeQuery = true)
    List<Object[]> findAverageByMetricSensorAndDate(List<Integer> metricIds, List<String> sensorIds, LocalDate start, LocalDate end);

    @Query(value = "select sensor_id,metric_id,avg(value) FROM sensor_reading WHERE metric_id in ?1 AND date BETWEEN ?2 AND ?3 GROUP BY sensor_id,metric_id;", nativeQuery = true)
    List<Object[]> findAverageByMetricsAndDates(List<Integer> metricIds, LocalDate startDate, LocalDate endDate);

    @Query(value = "select sensor_id,metric_id,(select value from sensor_reading s where s.sensor_id=sr.sensor_id AND s.metric_id=sr.metric_id ORDER BY date DESC LIMIT 1 ) as val  FROM sensor_reading as sr WHERE sr.metric_id in ?1  GROUP BY sr.sensor_id, sr.metric_id;", nativeQuery = true)
    List<Object[]> findLatestValueByMetrics(List<Integer> metricIds);

    @Query(value = "select sensor_id,metric_id,(select value from sensor_reading s where s.sensor_id=sr.sensor_id AND s.metric_id=sr.metric_id ORDER BY date DESC LIMIT 1 ) as val  FROM sensor_reading as sr WHERE sr.metric_id in ?1 AND sensor_id in ?2  GROUP BY sr.sensor_id, sr.metric_id;", nativeQuery = true)
    List<Object[]>findLatestValueByMetricsAndSensors(List<Integer> metricIds, List<String> sensorIds);
}
