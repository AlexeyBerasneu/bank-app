package com.bankapp.model;

import java.math.BigDecimal;

public class Account {

    private Integer id;
    private Integer userId;
    private BigDecimal accountAmount;

    public Account(Integer id, Integer userId, BigDecimal accountAmount) {
        this.id = id;
        this.userId = userId;
        this.accountAmount = accountAmount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public BigDecimal getAccountAmount() {
        return accountAmount;
    }

    public void setAccountAmount(BigDecimal accountAmount) {
        this.accountAmount = accountAmount;
    }

    public void increaseAccountAmount(BigDecimal amount) {
        accountAmount = accountAmount.add(amount);
    }
    public void decreaseAccountAmount(BigDecimal amount) {
        accountAmount = accountAmount.subtract(amount);
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", userId=" + userId +
                ", accountAmount=" + accountAmount +
                '}';
    }
}
