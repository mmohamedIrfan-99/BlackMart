package com.blackmart.blackmart.dto;

public class ErrorResponse {

    private boolean success;
    private String message;

    public ErrorResponse() {
    }

    public ErrorResponse(String message) {
        this.success = false;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
