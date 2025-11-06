package com.bankapp.exception;

public class NotEnoughMoneyException extends BankApplicationException{
    public NotEnoughMoneyException(String message) {
        super(message);
    }
}
