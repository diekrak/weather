package com.gsys.util;

public enum MessageResponse {
    SENSOR_ADDED(1,"The Sensor was added successfully"),
    SENSOR_ALREADY_REGISTERED(-1, "Sensor is already registered"),
    SENSOR_NOT_EXIST(-2, "Sensor Does not exists"),
    SENSOR_INACTIVE(1, "The Sensor is inactive" ),
    SENSOR_ACTIVE(1, "The Sensor is active" ),
    METRIC_NOT_EXIST(-3, "Sensor Does not exists"),
    READING_ADDED(1, "Reading Added Successfully" ),
    METRIC_REQUIRED(-5, "At least 1 metric ID must be provided" ),
    DATE_OUT_RANGE(-6, "The date range must not be higher than 30 Days" ),
    DATE_RANGE_INVALID(-5, "The start date must be before the end date" ), RESULTS_OK(1, "Results generated" );


    private int code;
    private String message;

    MessageResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
