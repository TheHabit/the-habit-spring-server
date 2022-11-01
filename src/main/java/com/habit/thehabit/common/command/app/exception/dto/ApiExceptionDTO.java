package com.habit.thehabit.common.command.app.exception.dto;

import org.springframework.http.HttpStatus;

public class ApiExceptionDTO {

    private int state;
    private String message;

    public ApiExceptionDTO(HttpStatus state, String message) {
        this.state = state.value();
        this.message = message;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ApiExceptionDTO{" +
                "state=" + state +
                ", message='" + message + '\'' +
                '}';
    }
}
