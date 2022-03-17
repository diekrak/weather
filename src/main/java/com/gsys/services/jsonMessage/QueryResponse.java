package com.gsys.services.jsonMessage;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gsys.util.MessageResponse;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public class QueryResponse {

    private int code;
    private String message;
    List<MetricResult> results;

    public QueryResponse() {
    }

    public QueryResponse(MessageResponse messageResponse) {
        this.code = messageResponse.getCode();
        this.message = messageResponse.getMessage();
    }

    public QueryResponse(int code, String message, List<MetricResult> results) {
        this.code = code;
        this.message = message;
        this.results = results;
    }

    public QueryResponse(MessageResponse messageResponse, List<MetricResult> averageByMetricSensorAndDate) {
        this.code = messageResponse.getCode();
        this.message = messageResponse.getMessage();
        this.results = averageByMetricSensorAndDate;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<MetricResult> getResults() {
        return results;
    }

    public void setResults(List<MetricResult> results) {
        this.results = results;
    }
}
