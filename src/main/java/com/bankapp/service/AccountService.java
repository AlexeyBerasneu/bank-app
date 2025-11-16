package com.bankapp.service;

import com.bankapp.exception.AccountException;
import com.bankapp.exception.NoAccountsException;
import com.bankapp.exception.NotEnoughMoneyException;
import com.bankapp.exception.UserNotFoundException;
import com.bankapp.model.Account;
import com.bankapp.model.User;
import com.bankapp.util.AccountProperties;
import com.bankapp.util.TransactionHelper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

@Service
public class AccountService {

    private final SessionFactory sessionFactory;
    private final TransactionHelper transactionHelper;
    private final AccountProperties accountProperties;

    public AccountService(SessionFactory sessionFactory, TransactionHelper transactionHelper, AccountProperties accountProperties) {
        this.sessionFactory = sessionFactory;
        this.transactionHelper = transactionHelper;
        this.accountProperties = accountProperties;
    }


    public Account saveAccount(Account account) {
        return transactionHelper.executeinTransaction(session -> {
            session.persist(account);
            return account;
        });
    }

    public Account createDefaultAccount(Long userId) {
        return transactionHelper.executeinTransaction(session -> {
            User user = session.find(User.class, userId);
            Account account = new Account(accountProperties.getBalance());
            user.addAccount(account);
            return account;
        });
    }

    public List<Account> getAllAccounts() {
        try (Session session = sessionFactory.openSession()) {
            List<Account> accountList = session.createQuery("from Account").list();
            if (accountList.isEmpty()) {
                throw new NoAccountsException("No accounts found");
            }
            return accountList;
        }
    }

    public Account getAccountById(Long accountId) {
        try (Session session = sessionFactory.openSession()) {
            Account account = session.find(Account.class, accountId);
            if (account == null) {
                throw new UserNotFoundException(accountId);
            }
            return account;
        }
    }


    public void deposit(Long id, BigDecimal amount) {
        transactionHelper.executeInTransaction(session -> {
            Account account = session.find(Account.class, id);
            account.increaseAccountAmount(amount);
        });
    }

    public boolean withdraw(Long id, BigDecimal amount) {
        BigDecimal tempAmount = getAccountById(id).getAccountAmount();
        if (tempAmount.compareTo(amount) == 1) {
            transactionHelper.executeInTransaction(session -> {
                Account account = session.find(Account.class, id);
                account.decreaseAccountAmount(amount);
            });
            return true;
        } else {
            throw new NotEnoughMoneyException("Executing command ACCOUNT_WITHDRAW: error = no such money to withdraw" +
                    "/ from account: id=" + id + ", moneyAmount=0" + ", attemptedWithdraw=" + amount);
        }
    }

    public boolean closeAccount(Long accountId) {
        return transactionHelper.executeinTransaction(session -> {
            Account fromAccount = session.find(Account.class, accountId);
            User user = fromAccount.getUser();
            List<Account> accountList = user.getAccountList();
            if (accountList.size() > 1) {
                Account targerAccount= accountList.stream()
                        .filter(acc -> !acc.getId().equals(accountId))
                        .sorted(Comparator.comparing(Account::getId))
                        .findFirst()
                        .orElseThrow(() -> new AccountException("No account found to transfer balance"));
                targerAccount.increaseAccountAmount(fromAccount.getAccountAmount());
                accountList.remove(fromAccount);
                fromAccount.setUser(null);
                session.remove(fromAccount);
                System.out.println("Account with ID " + accountId + " has been closed.");
                return true;
            } else {
                throw new AccountException("User with id " + user.getId() + " has only one account ! Operation declined!");
            }
        });
    }

    public void transferMoney(Long fromId, Long toId, BigDecimal amount) {
        transactionHelper.executeInTransaction(session -> {
            Account fromAccount = session.find(Account.class, fromId);
            Account toAccount = session.find(Account.class, toId);
            if (fromAccount.getAccountAmount().compareTo(amount) == -1 || fromId.equals(toId)) {
                throw new NotEnoughMoneyException("Don't have enough money in account ID " + fromId + " or you put the same account Id. Operation declined!");
            } else if (fromAccount.getUser().getId().equals(toAccount.getUser().getId())) {
                fromAccount.decreaseAccountAmount(amount);
                toAccount.increaseAccountAmount(amount);
            } else {
                fromAccount.decreaseAccountAmount(amount);
                BigDecimal commisionPercent = BigDecimal.valueOf(accountProperties.getCommission()).divide(BigDecimal.valueOf(100));
                BigDecimal finalAmount = amount.multiply(BigDecimal.ONE.subtract(commisionPercent));
                toAccount.increaseAccountAmount(finalAmount);
            }
        });
    }
}

