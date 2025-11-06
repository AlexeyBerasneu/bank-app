package com.bankapp.model;

import java.util.ArrayList;
import java.util.List;

public class User {

    private Integer id;
    private String login;
    private List<Account> accountList= new ArrayList<>();

    public User(Integer id, String login) {
        this.id = id;
        this.login = login;
    }

    public Integer getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public List<Account> getAccountList() {
        return accountList;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setAccountList(List<Account> accountList) {
        this.accountList = accountList;
    }

    public void addAccount(Account account) {
        accountList.add(account);
    }

    public void removeAccount(Integer accountId) {
       for (Account account : accountList) {
           if (account.getId().equals(accountId)) {
               accountList.remove(account);
           }
       }
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", accountList=" + accountList +
                '}';
    }
}
