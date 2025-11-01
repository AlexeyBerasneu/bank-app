package com.bankapp.service;

import com.bankapp.exception.AccountException;
import com.bankapp.exception.InvalidInputException;
import com.bankapp.exception.NotEnoughMoneyException;
import com.bankapp.exception.UserNotFoundException;
import com.bankapp.model.Account;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class AccountService {

    private final BigDecimal defaultAccountAmount;
    private final Double transferCommission;
    private final AtomicInteger idGenerator = new AtomicInteger(1);
    private final Map<Integer, Account> accounts;
    private final Scanner scanner;

    public AccountService(
            @Value("${account.default-amount}") BigDecimal defaultAccountAmount,
            @Value("${account.transfer-commission}") Double transferCommission) {
        this.defaultAccountAmount = defaultAccountAmount;
        this.transferCommission = transferCommission;
        this.scanner = new Scanner(System.in);
        this.accounts = new HashMap<>();
    }

    public Account createAccount(Integer userId) {
        if (checkAccountWithUserId(userId)) {
            Account account = new Account(idGenerator.getAndIncrement(), userId, defaultAccountAmount);
            accounts.put(account.getId(), account);
            return account;
        } else {
            throw new UserNotFoundException(userId);
        }
    }

    public Account createDefaultAccount(Integer userId) {
        Account account = new Account(idGenerator.getAndIncrement(), userId, defaultAccountAmount);
        accounts.put(account.getId(), account);
        return account;
    }

    public List<Account> getAllAccounts() {
        return new ArrayList<>(accounts.values());
    }

    public void deposit(Integer accountId, BigDecimal amount) {
        accounts.get(accountId).increaseAccountAmount(amount);
    }

    public void withdraw(Integer accountId, BigDecimal amount) {
        BigDecimal tempAmount = accounts.get(accountId).getAccountAmount();
        if (tempAmount.compareTo(amount) >= 0) {
            accounts.get(accountId).decreaseAccountAmount(amount);
        } else {
            throw new NotEnoughMoneyException("Error executing command ACCOUNT_WITHDRAW: error = no such money to withdraw" +
                    "/ from account: id=" + accountId + ", moneyAmount=0" + ", attemptedWithdraw=" + amount);
        }
    }

    public void closeAccount(Integer accountId) {
        Account account = accounts.get(accountId);
        Integer userId = account.getUserId();
        List<Account> accountListByUserId = getAllAccounts()
                .stream()
                .filter(acc -> acc.getUserId().equals(userId))
                .collect(Collectors.toList());
        ;
        if (accountListByUserId.size() > 1) {
            BigDecimal amount = accounts.get(accountId).getAccountAmount();
            Account targetAccount = accountListByUserId.stream()
                    .filter(acc -> !acc.getId().equals(account.getId()))
                    .sorted(Comparator.comparing(Account::getId))
                    .findFirst()
                    .orElseThrow(() -> new AccountException("No account found to transfer balance"));

            targetAccount.increaseAccountAmount(amount);
            accounts.remove(accountId);
            System.out.println("Account with ID " + accountId + " has been closed.");
        } else {
            throw new AccountException("User with id " + userId + " has only one account ! Operation declined!");
        }
    }

    public void transferMoney(Integer fromId, Integer toId, BigDecimal amount) {
        BigDecimal amountFromId = accounts.get(fromId).getAccountAmount();
        if (amountFromId.compareTo(amount) == -1) {
            throw new NotEnoughMoneyException("Account with ID " + fromId + " is not enough money to transfer.");
        } else if (checkAccountId(fromId, toId)) {
            accounts.get(fromId).decreaseAccountAmount(amount);
            accounts.get(toId).increaseAccountAmount(amount);
            System.out.println("Amount " + amount + " transferred from account ID " + fromId + " to account ID " + toId + ".");
        } else {
            accounts.get(fromId).decreaseAccountAmount(amount);
            BigDecimal commisionPercent=BigDecimal.valueOf(transferCommission).divide(BigDecimal.valueOf(100));
            BigDecimal finalAmount=amount.multiply(BigDecimal.ONE.subtract(commisionPercent));
            accounts.get(toId).increaseAccountAmount(finalAmount);
            System.out.println("Amount " + amount + " transferred from account ID " + fromId + " to account ID " + toId + ".");
        }
    }

    public Boolean checkAccountId(Integer fromId, Integer toId) {
        return accounts.get(fromId).getUserId().equals(accounts.get(toId).getUserId());
    }

    public Integer validAccountId() {
        try {
            Integer id = Integer.parseInt(scanner.nextLine().trim());
            if (accounts.containsKey(id)) {
                return id;
            } else {
                throw new UserNotFoundException(id);
            }
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Invalid input format !");
        }
    }

    public BigDecimal validAmount() {
        System.out.println();
        try {
            BigDecimal amount = BigDecimal.valueOf(Double.valueOf(scanner.nextLine().trim()));
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new InvalidInputException("Amount must be greater than zero");
            }
            return amount;
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Invalid input format !");
        }
    }

    private Boolean checkAccountWithUserId(Integer userId) {
        return getAllAccounts().stream().anyMatch(acc -> acc.getUserId().equals(userId));
    }
}
