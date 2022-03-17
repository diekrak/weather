package com.gsys.services.json;

import com.gsys.util.MessageResponse;

public class GenericResponse {

    Integer status;
    String message;

    public GenericResponse() {
        //Json Constructor
    }

    public GenericResponse(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public GenericResponse(MessageResponse messageResponse) {
        status = messageResponse.getCode();
        this.message = messageResponse.getMessage();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
