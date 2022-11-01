package com.habit.thehabit.common.command.app.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {super();}

    public UserNotFoundException(String message){
        super(message);
    }
}

