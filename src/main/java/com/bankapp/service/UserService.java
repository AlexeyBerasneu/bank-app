package com.bankapp.service;

import com.bankapp.exception.InvalidInputException;
import com.bankapp.exception.NoUsersException;
import com.bankapp.exception.UserAlreadyExistsException;
import com.bankapp.exception.UserNotFoundException;
import com.bankapp.model.User;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class UserService {

    private final AtomicInteger idGenerator = new AtomicInteger(1);
    private final Map<Integer, User> users;
    private final Scanner scanner;

    public UserService() {
        this.users = new HashMap<>();
        scanner = new Scanner(System.in);
    }

    public User createUser(String login) {
        if (login == null || login.isEmpty()) {
            throw new InvalidInputException("Login is null or empty!!!");
        } else if (existsUserByLogin(login)) {
            throw new UserAlreadyExistsException(login);
        } else {
            User user = new User(idGenerator.getAndIncrement(), login);
            users.put(user.getId(), user);
            return user;
        }
    }

    public List<User> getAll() {
        existsListOfUsers();
        return new ArrayList<>(users.values());
    }

    public User getUserById() {
        return users.get(validUserId());
    }

    public User getUserById(int id) {
        return users.get(id);
    }

    public boolean existsUserByLogin(String login) {
        return users.values().stream().anyMatch(user -> user.getLogin().equals(login));
    }

    public void existsListOfUsers() {
        if (users.isEmpty()) {
            throw new NoUsersException("No users found");
        }
    }

    public Integer validUserId() {
        try {
            Integer id = Integer.parseInt(scanner.nextLine().trim());
            if (getUserById(id) == null) {
               throw new UserNotFoundException(id);
            }
            return id;
        } catch (NumberFormatException e) {
               throw new InvalidInputException("Invalid input format !");
        }
    }

    public void deleteAccount(Integer accountId){
        for (User user : users.values()) {
            user.getAccountList().removeIf(acc->acc.getId().equals(accountId));
        }

    }
}
