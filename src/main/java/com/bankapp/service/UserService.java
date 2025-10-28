package com.bankapp.service;

import com.bankapp.model.Account;
import com.bankapp.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class UserService {

    private final AtomicInteger idGenerator = new AtomicInteger(1);
    private final AccountService accountService;
    private final Map<Integer, User> users;

    public UserService(AccountService accountService) {
        this.accountService = accountService;
        this.users = new HashMap<>();
    }

    public User creteUser(String login) {
        User user = new User(idGenerator.getAndIncrement(), login);
        Account account = accountService.createAccount(user.getId());
        user.addAccount(account);
        users.put(user.getId(), user);
        return user;
    }

    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    public User getUserById(int id) {
        return users.get(id);
    }

    public Account addAccount(Integer userId) {
        Account account = accountService.createAccount(userId);
        users.get(userId).addAccount(account);
        return account;
    }

    public boolean getUserByLogin(String login) {
        return users.values().stream().filter(user -> login.equals(user.getLogin())).findFirst().isPresent();
    }
}
