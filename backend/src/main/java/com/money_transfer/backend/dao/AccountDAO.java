package com.money_transfer.backend.dao;

import com.money_transfer.backend.models.Account;
import java.util.Optional;
import java.util.List;

public interface AccountDAO {
    String createAccount(Account account);
    Optional<Account> getAccount(Integer accountId);
    String updateAccount(Account account);
    String deleteAccount(Integer accountId);
    List<Account> getAllAccounts();
}

