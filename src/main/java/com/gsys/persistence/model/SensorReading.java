package com.gsys.persistence.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(indexes =@Index(name = "index1", columnList = "sensorId , metricId , date"))
public class SensorReading {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String sensorId;
    private Integer metricId;
    private Double value;
    private LocalDate date;

    public SensorReading() {
    }

    public SensorReading(String sensorId, Integer metricId, Double value, LocalDate date) {
        this.sensorId = sensorId;
        this.metricId = metricId;
        this.value = value;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSensorId() {
        return sensorId;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    public Integer getMetricId() {
        return metricId;
    }

    public void setMetricId(Integer metricId) {
        this.metricId = metricId;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
}
