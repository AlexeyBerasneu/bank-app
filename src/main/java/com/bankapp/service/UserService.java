package com.bankapp.service;

import com.bankapp.model.Account;
import com.bankapp.model.User;
import com.bankapp.util.AccountProperties;
import com.bankapp.util.TransactionHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final AccountService accountService;
    private final TransactionHelper transactionHelper;
    private final AccountProperties accountProperties;

    public UserService(AccountService accountService, TransactionHelper transactionHelper, AccountProperties accountProperties) {
        this.accountService = accountService;
        this.transactionHelper = transactionHelper;
        this.accountProperties = accountProperties;
    }

    public User creteUser(String login) {
       return transactionHelper.executeinTransaction(session -> {
            User user = new User(login);
            Account defaultAccount=new Account(accountProperties.getBalance());
            user.addAccount(defaultAccount);
            session.persist(user);
            return user;
        });
    }

    public List<User> getAll() {
        return transactionHelper.executeinTransaction(session -> {
            return session.createQuery("select u from User u join fetch u.accountList").list();
        });
    }

    public User getUserById(Long id) {
        return transactionHelper.executeinTransaction(session -> {
            return session.find(User.class, id);
        });
    }

    public boolean getUserByLogin(String login) {
        return transactionHelper.executeinTransaction(session -> {
            return session.createQuery("select u From User u where u.login = :login")
                    .setParameter("login", login)
                    .uniqueResult() != null;
        });
    }
}
