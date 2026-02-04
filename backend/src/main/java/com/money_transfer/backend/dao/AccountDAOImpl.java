package com.money_transfer.backend.dao;

import com.money_transfer.backend.models.Account;
import com.money_transfer.backend.models.AccountStatus;
import com.money_transfer.backend.exceptions.AccountNotFoundException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class AccountDAOImpl implements AccountDAO {

    private static List<Account> accounts = new ArrayList<>();

    // Static block to preload 10 accounts
    static {
        for (int i = 1; i <= 10; i++) {
            Account account = new Account();
            account.setId(i);
            account.setHolderName("User" + i);
            account.setBalance(1000.0 * i); // balances: 1000, 2000, ... 10000
            account.setStatus(AccountStatus.ACTIVE);
            account.setVersion(1);
            account.setLastUpdated(LocalDateTime.now());
            accounts.add(account);
        }
        System.out.println("✅ Preloaded 10 accounts in DAO");
    }

    @Override
    public String createAccount(Account account) {
        accounts.add(account);
        return "Account created successfully!";
    }

    @Override
    public Optional<Account> getAccount(Integer accountId) {
        for (Account account : accounts) {
            if (account.getId().equals(accountId)) {
                return Optional.of(account);
            }
        }
        throw new AccountNotFoundException("Account with ID " + accountId + " not found");
    }

    @Override
    public String updateAccount(Account account) {
        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).getId().equals(account.getId())) {
                accounts.set(i, account);
                return "Account updated successfully!";
            }
        }
        throw new AccountNotFoundException("Account with ID " + account.getId() + " not found");
    }

    @Override
    public String deleteAccount(Integer accountId) {
        for (Account account : accounts) {
            if (account.getId().equals(accountId)) {
                accounts.remove(account);
                return "Account deleted successfully!";
            }
        }
        throw new AccountNotFoundException("Account with ID " + accountId + " not found");
    }

    @Override
    public List<Account> getAllAccounts() {
        return accounts;
    }
}
