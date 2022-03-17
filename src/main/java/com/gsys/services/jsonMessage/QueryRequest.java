package com.gsys.services.jsonMessage;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class QueryRequest {

    List<String> sensorIds;
    List<Integer> metricIds;
    LocalDate startDate;
    LocalDate endDate;

    public QueryRequest() {
    }

    public List<String> getSensorIds() {
        return sensorIds;
    }

    public void setSensorIds(List<String> sensorIds) {
        this.sensorIds = sensorIds;
    }

    public List<Integer> getMetricIds() {
        return metricIds;
    }

    public void setMetricIds(List<Integer> metricIds) {
        this.metricIds = metricIds;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
