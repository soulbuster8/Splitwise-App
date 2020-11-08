package com.splitwise.exception;

public class ExpenseDoesNotExistException extends Exception{

    public ExpenseDoesNotExistException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
