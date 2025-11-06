package com.bankapp.exception;

public class UserAlreadyExistsException extends BankApplicationException {
    public UserAlreadyExistsException(String login) {
        super("User with login '"+login+"' already exists!");
    }
}
