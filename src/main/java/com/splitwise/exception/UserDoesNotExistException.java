package com.splitwise.exception;

public class UserDoesNotExistException extends Exception{

    public UserDoesNotExistException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
