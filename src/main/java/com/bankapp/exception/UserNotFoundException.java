package com.bankapp.exception;

public class UserNotFoundException extends BankApplicationException {
    public UserNotFoundException(Long id) {
        super("User not found with id =" + id);
    }
}
