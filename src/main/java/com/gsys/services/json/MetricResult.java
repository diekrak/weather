package com.gsys.services.json;

public class MetricResult {

    String sensorId;
    Integer metricId;
    Double value;

    public MetricResult() {
        //Json Constructor
    }

    public MetricResult(String sensorId, Integer metricId, Double value) {
        this.sensorId = sensorId;
        this.metricId = metricId;
        this.value = value;
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
}
