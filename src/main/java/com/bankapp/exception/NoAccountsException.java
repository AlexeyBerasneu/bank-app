package com.bankapp.exception;

public class NoAccountsException extends BankApplicationException{
    public NoAccountsException(String message) {
        super(message);
    }
}
