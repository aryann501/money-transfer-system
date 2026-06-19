package com.example.backend.services;

import com.example.backend.dtos.TransactionResponse;
import com.example.backend.entities.Account;
import com.example.backend.entities.TransactionDetails;
import com.example.backend.entities.TransactionLog;
import com.example.backend.enums.AccountStatus;
import com.example.backend.enums.TransactionCategory;
import com.example.backend.enums.TransactionStatus;
import com.example.backend.exceptions.*;
import com.example.backend.repositories.AccountRepository;
import com.example.backend.repositories.TransactionLogRepository;
import com.example.backend.security.service.UserDetailsImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TransferServiceImpl implements TransferService {

    private static final String SENDER_NOT_FOUND = "Sender account not found: %s";
    private static final String RECEIVER_NOT_FOUND = "Receiver account not found: %s";
    private static final String SENDER_NOT_ACTIVE = "Sender account is not active";
    private static final String RECEIVER_NOT_ACTIVE = "Receiver account is not active";
    private static final String INSUFFICIENT_BALANCE = "Insufficient balance in sender account";
    private static final String DUPLICATE_TRANSFER = "Duplicate transfer detected with idempotency key: %s";

    private final AccountRepository accountRepository;
    private final TransactionLogRepository transactionLogRepository;
private final RewardService rewardService;

    public TransferServiceImpl(AccountRepository accountRepository,
                           TransactionLogRepository transactionLogRepository,
                           RewardService rewardService) {
    this.accountRepository = accountRepository;
    this.transactionLogRepository = transactionLogRepository;
    this.rewardService = rewardService;
}


    @Override
    public TransactionResponse transfer(String fromAccountId,
                                        String toAccountId,
                                        Double amount,
                                        String idempotencyKey,
                                        String category,
                                        String note,
                                        Integer pointsToUse)
            throws AccountNotFoundException,
            AccountNotActiveException,
            InsufficientBalanceException,
            DuplicateTransferException {

        TransactionStatus transactionStatus = TransactionStatus.SUCCESS;
int points = 0;
Long userId = null;
String failureReason = null;
LocalDateTime now = LocalDateTime.now();

        Account fromAccount = accountRepository.findByAccountId(fromAccountId)
                .orElseThrow(() -> new AccountNotFoundException(
                        String.format(SENDER_NOT_FOUND, fromAccountId)));

        Account toAccount = accountRepository.findByAccountId(toAccountId)
                .orElseThrow(() -> new AccountNotFoundException(
                        String.format(RECEIVER_NOT_FOUND, toAccountId)));

        try {
            // Resolve points usage
            points = (pointsToUse != null) ? pointsToUse : 0;
            if (points < 0) {
                throw new IllegalArgumentException("Points to use must be >= 0");
            }
            // Get user id for reward points
            UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            userId = userDetails.getId();
            int availablePoints = rewardService.getAvailablePoints(userId);
            if (points > availablePoints) {
                throw new IllegalArgumentException("Points to use exceed available points");
            }
            if (points > amount.intValue()) {
                throw new IllegalArgumentException("Points to use exceed transfer amount");
            }
            double cashAmount = amount - points;
            if (cashAmount < 0) cashAmount = 0;
            validateAccounts(fromAccount, toAccount);
            validateBalance(fromAccount, cashAmount);
            validateIdempotency(idempotencyKey);

            performTransfer(fromAccount, toAccount, cashAmount);

        } catch (AccountNotActiveException |
                 InsufficientBalanceException e) {

            transactionStatus = TransactionStatus.FAILED;
            failureReason = e.getMessage();
        }

        TransactionCategory categoryEnum = resolveCategory(category);

        TransactionLog log = buildTransactionLog(
                fromAccount, toAccount, amount,
                transactionStatus, failureReason,
                idempotencyKey, now,
                categoryEnum, note, pointsToUse
        );

        transactionLogRepository.save(log);
        rewardService.processRewardForTransfer(log, fromAccount, toAccount);
        // Apply points redemption if any
        if (points > 0) {
            rewardService.processRewardRedemption(userId, String.valueOf(log.getId()), points);
        }

        return buildResponse(fromAccount, toAccount, amount,
                transactionStatus, failureReason, now,
                categoryEnum, note, pointsToUse);
    }

    private void validateAccounts(Account fromAccount, Account toAccount) {
        // Disallow transfers to the same account
        if (fromAccount.getAccountId().equals(toAccount.getAccountId())) {
            throw new IllegalArgumentException("You cannot transfer money to your own account");
        }
        if (fromAccount.getStatus() != AccountStatus.ACTIVE) {
            throw new AccountNotActiveException(SENDER_NOT_ACTIVE);
        }
        if (toAccount.getStatus() != AccountStatus.ACTIVE) {
            throw new AccountNotActiveException(RECEIVER_NOT_ACTIVE);
        }
    }

    private void validateBalance(Account fromAccount, Double amount) {
        if (fromAccount.getBalance() < amount) {
            throw new InsufficientBalanceException(INSUFFICIENT_BALANCE);
        }
    }

    private void validateIdempotency(String idempotencyKey) {
        if (transactionLogRepository.findByIdempotencyKey(idempotencyKey) != null) {
            throw new DuplicateTransferException(
                    String.format(DUPLICATE_TRANSFER, idempotencyKey));
        }
    }

    private void performTransfer(Account fromAccount, Account toAccount, Double amount) {
        fromAccount.setBalance(fromAccount.getBalance() - amount);
        toAccount.setBalance(toAccount.getBalance() + amount);

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
    }

    private TransactionCategory resolveCategory(String category) {
        if (category == null || category.isBlank()) {
            return TransactionCategory.OTHER;
        }
        try {
            return TransactionCategory.valueOf(category.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            return TransactionCategory.OTHER;
        }
    }

    private TransactionLog buildTransactionLog(Account fromAccount,
                                               Account toAccount,
                                               Double amount,
                                               TransactionStatus status,
                                               String failureReason,
                                               String idempotencyKey,
                                               LocalDateTime now,
                                               TransactionCategory category,
                                               String note, Integer pointsToUse) {

        TransactionLog log = new TransactionLog();
        log.setFromAccount(fromAccount);
        log.setToAccount(toAccount);
        log.setAmount(amount);
        log.setStatus(status);
        log.setFailureReason(failureReason);
        log.setIdempotencyKey(idempotencyKey);
        log.setCreatedOn(now);
        log.setPointToUse(pointsToUse);

        // attach details (separate table)
        TransactionDetails details = new TransactionDetails();
        details.setCategory(category);
        details.setNote(note);
        details.setTransactionLog(log);
        log.setDetails(details);

        return log;
    }

    private TransactionResponse buildResponse(Account fromAccount,
                                              Account toAccount,
                                              Double amount,
                                              TransactionStatus status,
                                              String failureReason,
                                              LocalDateTime now,
                                              TransactionCategory category,
                                              String note,
                                              Integer pointsToUse) {

        TransactionResponse response = new TransactionResponse();
        response.setFromAccountId(fromAccount.getAccountId());
        response.setFromAccountHolderName(fromAccount.getHolderName());
        response.setToAccountId(toAccount.getAccountId());
        response.setToAccountHolderName(toAccount.getHolderName());
        response.setAmount(amount);
        response.setStatus(status.name());
        response.setFailureReason(failureReason);
        response.setCreatedOn(now);
        response.setCategory(category != null ? category.name() : null);
        response.setNote(note);
        response.setPointsToUse(pointsToUse);

        return response;
    }
}
