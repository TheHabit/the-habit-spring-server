package com.habit.thehabit.record.command.app.exception;

public class DuplicateRecordException extends RuntimeException {

    public DuplicateRecordException(){
        super();
    }

    public DuplicateRecordException(String msg) {
        super(msg);
    }
}
