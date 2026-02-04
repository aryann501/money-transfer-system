package com.example.backend.services;

import com.example.backend.entity.Account;
import com.example.backend.exceptions.AccountNotFoundException;
import com.example.backend.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    public AccountRepository repo;

    @Override
    public Account createAccount(Account account) {
        return repo.save(account);
    }

    @Override
    public Account getAccountById(Integer accountId) throws AccountNotFoundException {
        Optional<Account> acc = repo.findById(accountId);
        if(acc.isPresent()) {
            return acc.get();
        }
        throw new AccountNotFoundException("Account with id "+accountId+" not found");
    }

    @Override
    public Account updateAccount(Account account) throws AccountNotFoundException {
        if(repo.existsById(account.getId())) {
            return repo.save(account);
        }
        throw new AccountNotFoundException("Account with id: "+account.getId()+" not exists");
    }

    @Override
    public void deleteAccount(Account account) throws AccountNotFoundException {
        if(repo.existsById(account.getId())) {
            repo.delete(account);
        }
        throw new AccountNotFoundException("Account with id: "+account.getId()+" not exists");
    }
}
