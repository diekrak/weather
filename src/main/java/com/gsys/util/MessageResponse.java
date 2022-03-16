package com.gsys.util;

public enum MessageResponse {
    SENSOR_ADDED(1,"The Sensor was added successfully"),
    SENSOR_EXIST(-1, "Sensor is already registered");


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
