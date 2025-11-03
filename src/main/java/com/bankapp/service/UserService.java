package com.bankapp.service;

import com.bankapp.exception.InvalidInputException;
import com.bankapp.exception.NoUsersException;
import com.bankapp.exception.UserAlreadyExistsException;
import com.bankapp.exception.UserNotFoundException;
import com.bankapp.model.Account;
import com.bankapp.model.User;
import com.bankapp.util.AccountProperties;
import com.bankapp.util.TransactionHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {


    private final TransactionHelper transactionHelper;
    private final AccountProperties accountProperties;

    public UserService(TransactionHelper transactionHelper, AccountProperties accountProperties) {
        this.transactionHelper = transactionHelper;
        this.accountProperties = accountProperties;
    }

    public User createUser(String login) {
        if (login == null || login.isEmpty()) {
            throw new InvalidInputException("Login is null or empty!!!");
        } else if (getUserByLogin(login)) {
            throw new UserAlreadyExistsException(login);
        } else {
            return transactionHelper.executeinTransaction(session -> {
                User user = new User(login);
                Account defaultAccount = new Account(accountProperties.getBalance());
                user.addAccount(defaultAccount);
                session.persist(user);
                return user;
            });
        }
    }

    public List<User> getAll() {
        return transactionHelper.executeinTransaction(session -> {
            List<User> userList= session.createQuery("select u from User u join fetch u.accountList order by u.login").list();
            if(userList.isEmpty()) {
                throw new NoUsersException("No users found");
            }
            return userList;
        });
    }

    public User getUserById(Long id) {
        return transactionHelper.executeinTransaction(session -> {
            User user = session.find(User.class, id);
            if (user == null) {
                throw new UserNotFoundException(id);
            }
            return user;
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
