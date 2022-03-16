package com.gsys.persistence.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(indexes =@Index(name = "index1", columnList = "sensorId , metricId , date"))
public class SensorReading {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String sensorId;
    private Integer metricId;
    private Long value;

    private Date date;

    public SensorReading(String sensorId, Integer metricId, Long value, Date date) {
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

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
}
