package com.bankapp.exception;

public class NoUsersException extends BankApplicationException {
    public NoUsersException(String message) {
        super(message);
    }
}
