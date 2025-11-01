package com.bankapp.util;

import com.bankapp.model.Account;
import com.bankapp.model.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class Printer {
    public void printListOfUsers(List<User> users) {
        for (User user : users) {
            System.out.println(user);
        }
    }

    public void printListOfUsersWithId(List<User> users) {
        users.stream().forEach(user -> {
            System.out.print(user.getId() + " ");
        });
    }

    public void printUsersWithAccounts(List<User> users) {
        users
                .stream()
                .map(user -> user.getLogin() + " [ Account ID's: " + user.getAccountList()
                        .stream()
                        .map(account -> "Id - " + (account.getId() + " - available: " + account.getAccountAmount()))
                        .collect(Collectors.joining(", "))
                        + "]")
                .forEach(System.out::println);
    }

    public void printMenu() {
        System.out.println("\nMenu");
        System.out.println("1 - USER_CREATE");
        System.out.println("2 - SHOW_ALL_USERS");
        System.out.println("3 - ACCOUNT_CREATE");
        System.out.println("4 - ACCOUNT_DEPOSIT");
        System.out.println("5 - ACCOUNT_WITHDRAW");
        System.out.println("6 - ACCOUNT_TRANSFER");
        System.out.println("7 - ACCOUNT_CLOSE");
        System.out.println("8 - EXIT");
        System.out.println("Please enter number of operation:");
    }
}
