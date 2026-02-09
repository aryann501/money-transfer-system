package com.example.backend.services;

import com.example.backend.dtos.AccountDTO;
import com.example.backend.entities.Account;
import com.example.backend.entities.TransactionLog;
import com.example.backend.dtos.TransactionResponse;
import com.example.backend.repositories.AccountRepository;
import com.example.backend.exceptions.AccountNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public AccountDTO getAccount(String id) {
        Account account = accountRepository.findByAccountId(id)
                .orElseThrow(() -> new AccountNotFoundException("Account with id " + id + " not found"));
        // Convert the Account entity to AccountDTO directly
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(account.getId());
        accountDTO.setHolderName(account.getHolderName());
        accountDTO.setAccountId(account.getAccountId());
        accountDTO.setBalance(account.getBalance());
        accountDTO.setStatus(account.getStatus().name()); // Assuming AccountStatus is an Enum
        accountDTO.setVersion(account.getVersion());
        accountDTO.setLastUpdated(account.getLastUpdated());

        // Return the AccountDTO
        return accountDTO;
    }

    @Override
    public Double getBalance(String id) throws AccountNotFoundException {
        Account account = accountRepository.findByAccountId(id)
                .orElseThrow(() -> new AccountNotFoundException("Account with id " + id + " not found"));
        return account.getBalance();
    }

    @Override
    public List<TransactionResponse> getTransactions(String id) throws AccountNotFoundException {
        // Fetch the account using the provided ID
        Account account = accountRepository.findByAccountId(id)
                .orElseThrow(() -> new AccountNotFoundException("Account with id " + id + " not found"));

        // Create a list to hold all transactions (outgoing + incoming)
        List<TransactionLog> allTransactions = new ArrayList<>();
        allTransactions.addAll(account.getOutgoingTransactions());
        allTransactions.addAll(account.getIncomingTransactions());

        // Create a list to hold the transaction response DTOs
        List<TransactionResponse> transactionResponseList = new ArrayList<>();

        // Convert each transaction (both incoming and outgoing) into a TransactionResponse DTO
        for (TransactionLog transaction : allTransactions) {
            TransactionResponse response = new TransactionResponse();
            response.setFromAccountId(transaction.getFromAccount().getAccountId());
            response.setFromAccountHolderName(transaction.getFromAccount().getHolderName());
            response.setToAccountId(transaction.getToAccount().getAccountId());
            response.setToAccountHolderName(transaction.getToAccount().getHolderName());
            response.setAmount(transaction.getAmount());
            response.setStatus(transaction.getStatus().name());  // Assuming status is an enum (SUCCESS or FAILED)
            response.setCreatedOn(transaction.getCreatedOn());

            transactionResponseList.add(response);
        }

        // Return the list of TransactionResponse DTOs
        return transactionResponseList;
    }
    public String generateAccountId() {
        return "ACC" + String.format("%04d", new Random().nextInt(10000));
    }
}
