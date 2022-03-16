package com.gsys.services.jsonMessage;

import com.gsys.util.MessageResponse;

public class GenericResponse {

    Integer Status;
    String message;

    public GenericResponse() {
    }

    public GenericResponse(Integer status, String message) {
        Status = status;
        this.message = message;
    }

    public GenericResponse(MessageResponse messageResponse) {
        Status = messageResponse.getCode();
        this.message = messageResponse.getMessage();
    }

    public Integer getStatus() {
        return Status;
    }

    public void setStatus(Integer status) {
        Status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
