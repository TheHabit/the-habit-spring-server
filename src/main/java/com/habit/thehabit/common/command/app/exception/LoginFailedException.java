package com.habit.thehabit.common.command.app.exception;

public class LoginFailedException extends Throwable {

    public LoginFailedException() {
        super();
    }
    public LoginFailedException(String message) {
        super(message);
    }
}
