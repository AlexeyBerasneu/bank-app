package com.bankapp.exception;

public class AccountNotFoundException extends BankApplicationException{
    public AccountNotFoundException(Integer id) {
        super("Account not found with id =" + id);
    }
}
