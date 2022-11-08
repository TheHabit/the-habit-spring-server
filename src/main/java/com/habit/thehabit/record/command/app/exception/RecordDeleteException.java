package com.habit.thehabit.record.command.app.exception;

public class RecordDeleteException extends RuntimeException {

    public RecordDeleteException(){
        super();
    }
    public RecordDeleteException(String msg) {
        super(msg);
    }
}
