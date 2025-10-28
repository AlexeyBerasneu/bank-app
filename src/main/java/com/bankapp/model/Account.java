package com.bankapp.model;

public class Account {

    private Integer id;
    private Integer userId;
    private Double accountAmount;

    public Account(Integer id, Integer userId, Double accountAmount) {
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

    public Double getAccountAmount() {
        return accountAmount;
    }

    public void setAccountAmount(Double accountAmount) {
        this.accountAmount = accountAmount;
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
