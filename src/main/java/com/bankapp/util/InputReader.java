package com.bankapp.util;

import com.bankapp.exception.IdException;
import com.bankapp.exception.InvalidInputException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Scanner;

@Component
public class InputReader {
    private final Scanner scanner;

    public InputReader() {
        this.scanner = new Scanner(System.in);
    }

    public BigDecimal readAmount() {
        try {
            BigDecimal amount = BigDecimal.valueOf(Double.valueOf(scanner.nextLine().trim()));
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new InvalidInputException("Amount must be greater than zero");
            }
            return amount;
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Invalid input format !");
        }
    }

    public Long readLongInput() {
        try {
            Long id = Long.parseLong(scanner.nextLine().trim());
            if (id <= 0) {
                throw new IdException("ID must be a positive number!");
            }
            return id;
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Invalid input format !");
        }
    }
}
