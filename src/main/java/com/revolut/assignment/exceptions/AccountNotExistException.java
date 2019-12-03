package com.revolut.assignment.exceptions;

public class AccountNotExistException extends RuntimeException {
    public AccountNotExistException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
