package com.bankapp.util;

import com.bankapp.exception.BankApplicationException;
import org.springframework.stereotype.Component;

@Component
public class Handler {

    public void handle(Runnable runnable) {
        try {
            runnable.run();
        } catch (BankApplicationException e) {
            System.out.println("Error : " + e.getMessage());
        }
    }
}
