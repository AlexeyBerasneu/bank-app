package com.bankapp.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "account_amount", precision = 10, scale = 2)
    private BigDecimal accountAmount;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;

    public Account() {
    }

    public Account(BigDecimal accountAmount) {
        this.accountAmount = accountAmount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAccountAmount() {
        return accountAmount;
    }

    public void setAccountAmount(BigDecimal accountAmount) {
        this.accountAmount = accountAmount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void increaseAccountAmount(BigDecimal amount) {
        this.accountAmount = this.accountAmount.add(amount);
    }

    public void decreaseAccountAmount(BigDecimal amount) {
        this.accountAmount = this.accountAmount.subtract(amount);
    }

    @Override
    public String toString() {
        return user + " - { account id=" + id +
                ", accountAmount=" + accountAmount + " }";
    }
}
