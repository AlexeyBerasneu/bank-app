package com.bankapp.exception;

public class InvalidInputException extends BankApplicationException {
    public InvalidInputException(String message) {
        super(message);
    }
}
