package com.revolut.assignment;

public class NoEnoughMoneyException extends RuntimeException {
    public NoEnoughMoneyException(String errorMessage) {
        super(errorMessage);
    }
}
