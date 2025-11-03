package com.bankapp.exception;

public class BankApplicationException extends RuntimeException {
    public BankApplicationException(String message) {
        super(message);
    }
}
