package com.bankapp.exception;

public class UserNotFoundException extends BankApplicationException {
    public UserNotFoundException(Integer id) {
        super("User not found with id =" + id);
    }
}
