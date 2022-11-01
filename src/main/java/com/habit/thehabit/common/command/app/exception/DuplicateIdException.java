package com.habit.thehabit.common.command.app.exception;

public class DuplicateIdException extends RuntimeException {

    public DuplicateIdException(){super();}
    public DuplicateIdException(String message) {
        super(message);
    }


}
