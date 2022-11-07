package com.habit.thehabit.record.command.app.exception;

public class RecordNotFoundException extends RuntimeException {

    public RecordNotFoundException(){super();}
    public RecordNotFoundException(String message) {
        super(message);
    }
}
