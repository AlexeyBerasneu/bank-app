package com.bankapp.service;

import com.bankapp.model.Account;
import com.bankapp.model.User;
import com.bankapp.util.Handler;
import com.bankapp.util.InputReader;
import com.bankapp.util.Printer;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Scanner;

@Service
public class OperationsConsoleListener {

    private final AccountService accountService;
    private final UserService userService;
    private final Handler handler;
    private final Printer printer;
    private final InputReader inputReader;
    private final Scanner scanner;

    public OperationsConsoleListener(UserService userService, AccountService accountService, Handler handler, Printer printer, InputReader inputReader) {
        this.userService = userService;
        this.accountService = accountService;
        this.handler = handler;
        this.printer = printer;
        this.inputReader = inputReader;
        scanner = new Scanner(System.in);
    }

    public void listen() {

        while (true) {
            printer.printMenu();
            String operation = scanner.nextLine().trim();
            switch (operation) {
                //"USER_CREATE"
                case "1" -> handler.handle(() -> {
                    System.out.println("USER_CREATE");
                    System.out.println("Enter login for new user:");
                    String login = scanner.nextLine().trim().toLowerCase();
                    User user = userService.createUser(login);
                    System.out.println("User created: " + user);
                });
                //"SHOW_ALL_USERS"
                case "2" -> handler.handle(() -> {
                    {
                        System.out.println("SHOW_ALL_USERS");
                        System.out.println("List of all users:");
                        printer.printUsersWithAccounts(userService.getAll());
                    }
                });
                //"ACCOUNT_CREATE"
                case "3" -> handler.handle(() -> {
                    System.out.println("ACCOUNT_CREATE");
                    System.out.print("Enter the user id for which to create an account.\n" +
                            "List of User id's: ");
                    printer.printListOfUsersWithId(userService.getAll());
                    System.out.println();
                    Long userId = inputReader.readLongInput();
                    User user = userService.getUserById(userId);
                    Account account = accountService.createDefaultAccount(userId);
                    System.out.println("New account created with ID: "
                            + account.getId() + " for user: "
                            + user.getLogin());
                });
                //"ACCOUNT_DEPOSIT"
                case "4" -> handler.handle(() -> {
                    System.out.println("ACCOUNT_DEPOSIT");
                    System.out.println("Enter account ID  from list:");
                    printer.printUsersWithAccounts(userService.getAll());
                    Long accountId = inputReader.readLongInput();
                    Account account = accountService.getAccountById(accountId);
                    System.out.print("Enter amount to deposit: ");
                    BigDecimal amount = inputReader.readAmount();
                    accountService.deposit(accountId, amount);
                    System.out.println("Amount " + amount + " deposited to account ID: " + account.getId());

                });
                //"ACCOUNT_WITHDRAW"
                case "5" -> handler.handle(() -> {
                    System.out.println("ACCOUNT_WITHDRAW");
                    System.out.println("Enter account ID to withdraw from list: ");
                    printer.printUsersWithAccounts(userService.getAll());
                    Long accountId = inputReader.readLongInput();
                    Account account = accountService.getAccountById(accountId);
                    System.out.print("Enter amount to withdraw: ");
                    BigDecimal amount = inputReader.readAmount();
                    accountService.withdraw(account.getId(), amount);
                    System.out.println("Withdraw successful !!!");
                });
                //"ACCOUNT_TRANSFER"
                case "6" -> handler.handle(() -> {
                    System.out.println("ACCOUNT_TRANSFER");
                    printer.printUsersWithAccounts(userService.getAll());
                    System.out.print("Enter source account ID from list: ");
                    Long fromAccountId = inputReader.readLongInput();
                    Account sourceAccount = accountService.getAccountById(fromAccountId);
                    System.out.print("Enter target account ID:");
                    Long toAccountId = inputReader.readLongInput();
                    Account targetAccount = accountService.getAccountById(toAccountId);
                    System.out.print("Enter amount to transfer:");
                    BigDecimal amount = inputReader.readAmount();
                    accountService.transferMoney(fromAccountId, toAccountId, amount);
                    System.out.println("Amount " + amount + " transferred from account ID " + fromAccountId + " to account ID " + toAccountId + ".");
                });
                //"ACCOUNT_CLOSE"
                case "7" -> handler.handle(() -> {
                    {
                        System.out.println("ACCOUNT_CLOSE");
                        System.out.println("Enter account ID to close from list: ");
                        printer.printUsersWithAccounts(userService.getAll());
                        Long accountId = inputReader.readLongInput();
                        Account account = accountService.getAccountById(accountId);
                        accountService.closeAccount(accountId);
                    }
                });
                //"EXIT"
                case "8" -> {
                    return;
                }
                default -> {
                    System.out.println("You entered invalid operation: " + operation);
                    System.out.println("Please try again.");
                }
            }
        }
    }
}
