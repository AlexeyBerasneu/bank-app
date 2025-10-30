package com.bankapp.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AccountProperties {

    @Value("${account.default-amount}")
    private BigDecimal balance;

    @Value("${accaount.transfer-commission}")
    private Double commission;

    public BigDecimal getBalance() {
        return balance;
    }

    public Double getCommission() {
        return commission;
    }
}
