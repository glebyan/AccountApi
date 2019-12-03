package com.revolut.assignment.exceptions;

public class NotEnoughMoneyException extends RuntimeException {
    public NotEnoughMoneyException(String errorMessage) {
        super(errorMessage);
    }
}
