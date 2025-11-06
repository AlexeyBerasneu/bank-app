package com.bankapp.service;

import com.bankapp.model.Account;
import com.bankapp.model.User;
import com.bankapp.util.Handler;
import com.bankapp.util.Printer;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Scanner;

@Service
public class OperationsConsoleListener {

    private final UserService userService;
    private final AccountService accountService;
    private final Handler handler;
    private final Printer printer;
    private final Scanner scanner;

    public OperationsConsoleListener(UserService userService, AccountService accountService, Handler handler, Printer printer) {
        this.userService = userService;
        this.accountService = accountService;
        this.handler = handler;
        this.printer = printer;
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
                    Account account=accountService.createDefaultAccount(user.getId());
                    user.addAccount(account);
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
                    //Validation userId
                    User user = userService.getUserById();
                    Account account = accountService.createAccount(user.getId());
                    user.addAccount(account);
                    System.out.println("New account created with ID: "
                            + account.getId() + " for user: "
                            + user.getLogin());
                });
                //"ACCOUNT_DEPOSIT"
                case "4" -> handler.handle(() -> {
                    System.out.println("ACCOUNT_DEPOSIT");
                    System.out.println("Enter account ID  from list:");
                    printer.printUsersWithAccounts(userService.getAll());
                    Integer accountId =  accountService.validAccountId();
                    System.out.print("Enter amount to deposit: ");
                    BigDecimal amount = accountService.validAmount();
                    accountService.deposit(accountId, amount);
                    System.out.println("Amount " + amount + " deposited to account ID: " + accountId);

                });
                //"ACCOUNT_WITHDRAW"
                case "5" -> handler.handle(() ->{
                    System.out.println("ACCOUNT_WITHDRAW");
                    System.out.println("Enter account ID to withdraw from list: ");
                    printer.printUsersWithAccounts(userService.getAll());
                    Integer accountId = accountService.validAccountId();
                    System.out.print("Enter amount to withdraw: ");
                    BigDecimal amount = accountService.validAmount();
                    accountService.withdraw(accountId,amount);
                    System.out.println("Withdraw successful !!!");
                }) ;
                //"ACCOUNT_TRANSFER"
                case "6" -> handler.handle(() ->{
                    System.out.println("ACCOUNT_TRANSFER");
                    printer.printUsersWithAccounts(userService.getAll());
                    System.out.print("Enter source account ID from list: ");
                    Integer fromAccount = accountService.validAccountId();
                    System.out.print("Enter target account ID:");
                    Integer toAccount = accountService.validAccountId();
                    System.out.print("Enter amount to transfer:");
                    BigDecimal amount = accountService.validAmount();
                    accountService.transferMoney(fromAccount, toAccount, amount);
                }) ;
                //"ACCOUNT_CLOSE"
                case "7" -> handler.handle(() -> {
                    {
                        System.out.println("ACCOUNT_CLOSE");
                        System.out.println("Enter account ID to close from list: ");
                        printer.printUsersWithAccounts(userService.getAll());
                        Integer accountId = accountService.validAccountId();
                        accountService.closeAccount(accountId);
                        userService.deleteAccount(accountId);
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
