package com.bankapp.service;

import com.bankapp.model.Account;
import com.bankapp.model.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Scanner;
import java.util.stream.Collectors;

@Service
public class OperationsConsoleListener {

    private final UserService userService;
    private final AccountService accountService;
    private final String warning = "!!! WARNING !!!";
    private final Scanner scanner;

    public OperationsConsoleListener(UserService userService, AccountService accountService) {
        this.userService = userService;
        this.accountService = accountService;
        scanner = new Scanner(System.in);
    }

    public void listen() {

        while (true) {
            printMenu();
            String operation = scanner.nextLine().trim();
            switch (operation) {
                //"USER_CREATE"
                case "1" -> {
                    System.out.println("USER_CREATE");
                    System.out.println("Enter login for new user:");
                    String login = scanner.nextLine().trim().toLowerCase();
                    if (!validLogin(login)) {
                        break;
                    }
                    System.out.println("User created: " + userService.creteUser(login));
                }
                //"SHOW_ALL_USERS"
                case "2" -> {
                    System.out.println("SHOW_ALL_USERS");
                    System.out.println("List of all users:");
                    if (!validListOfAccounts()) {
                        System.out.println(warning);
                        System.out.println("No users found");
                        break;
                    }
                    for (User user : userService.getAll()) {
                        System.out.println(user);
                        user.getAccountList().forEach(account -> System.out.println(account));
                    }
                }
                //"ACCOUNT_CREATE"
                case "3" -> {
                    System.out.println("ACCOUNT_CREATE");
                    if (!validListOfAccounts()) {
                        break;
                    }
                    System.out.print("Enter the user id for which to create an account.\n" +
                            "List of User id's: ");
                    Long id = validUserId();
                    if (id == null) {
                        break;
                    }
                    Account account = accountService.createDefaultAccount(id);
                    System.out.println("New account created with ID: "
                            + account.getId() + " for user: "
                            + userService.getUserById(id).getLogin());
                }
                //"ACCOUNT_DEPOSIT"
                case "4" -> {
                    System.out.println("ACCOUNT_DEPOSIT");
                    if (!validListOfAccounts()) {
                        break;
                    }
                    System.out.println("Enter account ID  from list:");
                    Long id = validAccountId();
                    if (id == null) {
                        break;
                    }
                    System.out.print("Enter amount to deposit: ");
                    BigDecimal amount = BigDecimal.valueOf(validAmount());
                    if (amount == null) {
                        break;
                    }
                    accountService.deposit(id, amount);
                    System.out.println("Amount " + amount + " deposited to account ID: " + id);
                }
                //"ACCOUNT_WITHDRAW"
                case "5" -> {
                    System.out.println("ACCOUNT_WITHDRAW");
                    if (!validListOfAccounts()) {
                        break;
                    }
                    System.out.print("Enter account ID to withdraw from list: ");
                    Long id = validAccountId();
                    if (id == null) {
                        break;
                    }
                    System.out.print("Enter amount to withdraw: ");
                    BigDecimal amount =BigDecimal.valueOf(validAmount());
                    if (amount == null) {
                        break;
                    }
                    if (!accountService.withdraw(id, amount)) {
                        break;
                    }
                    System.out.println("Withdraw successful !!!");
                }
                //"ACCOUNT_TRANSFER"
                case "6" -> {
                    System.out.println("ACCOUNT_TRANSFER");
                    if (!validListOfAccounts()) {
                        break;
                    }
                    System.out.println("Enter source account ID from list: ");
                    Long idSource = validAccountId();
                    if (idSource == null) {
                        break;
                    }
                    System.out.print("Enter target account ID:");
                    Long idTarget = validAccountId();
                    if (idTarget == null) {
                        break;
                    }
                    System.out.print("Enter amount to transfer:");
                    BigDecimal amount = BigDecimal.valueOf(validAmount());
                    if (amount == null) {
                        break;
                    }
                    accountService.transferMoney(idSource, idTarget, amount);
                }
                //"ACCOUNT_CLOSE"
                case "7" -> {
                    System.out.println("ACCOUNT_CLOSE");
                    if (!validListOfAccounts()) {
                        break;
                    }
                    System.out.println("Enter account ID to close from list: ");
                    Long id = validAccountId();
                    if (id == null) {
                        break;
                    }
                    accountService.closeAccount(id);
                }
                //"EXIT"
                case "8" -> {
                    return;
                }
                default -> {
                    System.out.println("\n" + warning);
                    System.out.println("You entered invalid operation: " + operation);
                    System.out.println("Please try again.");
                }
            }
        }
    }

    private void printMenu() {
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

    private Boolean validLogin(String login) {
        if (login == null || login.isEmpty() || userService.getUserByLogin(login)) {
            System.out.println(warning);
            System.out.println("Login already exists or wrong!!!");
            return false;
        }
        return true;
    }

    private Long validUserId() {
        userService.getAll().stream().forEach(user -> {
            System.out.print(user.getId() + " ");
        });
        System.out.println();
        try {
            Long id = Long.parseLong(scanner.nextLine().trim());
            if (userService.getUserById(id) == null) {
                System.out.println(warning);
                System.out.println("User not found");
                return null;
            }
            return id;
        } catch (NumberFormatException e) {
            System.out.println(warning);
            System.out.println("Invalid number format of ID.");
            return null;
        }
    }

    private Double validAmount() {
        System.out.println();
        try {
            Double amount = Double.parseDouble(scanner.nextLine().trim());
            if (amount < 0) {
                System.out.println(warning);
                System.out.println("Invalid amount entered.");
                return null;
            }
            return amount;
        } catch (NumberFormatException e) {
            System.out.println(warning);
            System.out.println("Invalid number format of amount.");
            return null;
        }
    }

    private Long validAccountId() {
        userService.getAll()
                .stream()
                .map(user -> user.getLogin() + " [ Account ID's: " + user.getAccountList()
                                .stream()
                                .map(account -> "Id - " + (account.getId() + " - available: " + account.getAccountAmount()))
                                .collect(Collectors.joining(", "))
                        + "]")
                .forEach(System.out::println);
        try {
            Long id = Long.parseLong(scanner.nextLine().trim());
            if (accountService.getAccountById(id) == null) {
                System.out.println(warning);
                System.out.println("Account not found");
                return null;
            }
            return id;
        } catch (NumberFormatException e) {
            System.out.println(warning);
            System.out.println("Invalid number format of ID.");
            return null;
        }
    }

    private Boolean validListOfAccounts() {
        if (userService.getAll().isEmpty()) {
            System.out.println(warning);
            System.out.println("List of users is empty !!!");
            return false;
        }
        return true;
    }
}
