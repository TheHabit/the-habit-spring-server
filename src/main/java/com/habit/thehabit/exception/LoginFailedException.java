package com.habit.thehabit.exception;

public class LoginFailedException extends Throwable {

    public LoginFailedException() {
        super();
    }
    public LoginFailedException(String message) {
        super(message);
    }
}
