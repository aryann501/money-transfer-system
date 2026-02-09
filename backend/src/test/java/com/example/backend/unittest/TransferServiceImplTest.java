package com.example.backend.unittest;

import com.example.backend.dtos.TransactionResponse;
import com.example.backend.entities.Account;
import com.example.backend.entities.TransactionLog;
import com.example.backend.enums.AccountStatus;
import com.example.backend.exceptions.AccountNotFoundException;
import com.example.backend.repositories.AccountRepository;
import com.example.backend.repositories.TransactionLogRepository;
import com.example.backend.services.TransferServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TransferServiceImplTest {

    @InjectMocks
    private TransferServiceImpl transferService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionLogRepository transactionLogRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testTransfer_success() {
        // Arrange
        String fromId = "ACC1111";
        String toId = "ACC2222";
        Double amount = 100.0;
        String idempotencyKey = "key123";

        Account fromAccount = new Account();
        fromAccount.setAccountId(fromId);
        fromAccount.setBalance(500.0);
        fromAccount.setStatus(AccountStatus.ACTIVE);
        fromAccount.setHolderName("Sender");

        Account toAccount = new Account();
        toAccount.setAccountId(toId);
        toAccount.setBalance(200.0);
        toAccount.setStatus(AccountStatus.ACTIVE);
        toAccount.setHolderName("Receiver");

        when(accountRepository.findByAccountId(fromId)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findByAccountId(toId)).thenReturn(Optional.of(toAccount));
        when(transactionLogRepository.findByIdempotencyKey(idempotencyKey)).thenReturn(null);
        when(transactionLogRepository.save(any(TransactionLog.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        TransactionResponse response = transferService.transfer(fromId, toId, amount, idempotencyKey);

        // Assert
        assertEquals("SUCCESS", response.getStatus());
        assertEquals(400.0, fromAccount.getBalance());
        assertEquals(300.0, toAccount.getBalance());
        verify(accountRepository, times(1)).save(fromAccount);
        verify(accountRepository, times(1)).save(toAccount);
        verify(transactionLogRepository, times(1)).save(any(TransactionLog.class));
    }

    @Test
    void testTransfer_senderNotFound() {
        // Arrange
        String fromId = "ACC1111";
        String toId = "ACC2222";
        Double amount = 100.0;
        String idempotencyKey = "key123";

        when(accountRepository.findByAccountId(fromId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AccountNotFoundException.class, () -> 
            transferService.transfer(fromId, toId, amount, idempotencyKey));
    }

    @Test
    void testTransfer_receiverNotFound() {
        // Arrange
        String fromId = "ACC1111";
        String toId = "ACC2222";
        Double amount = 100.0;
        String idempotencyKey = "key123";

        Account fromAccount = new Account();
        fromAccount.setAccountId(fromId);

        when(accountRepository.findByAccountId(fromId)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findByAccountId(toId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AccountNotFoundException.class, () -> 
            transferService.transfer(fromId, toId, amount, idempotencyKey));
    }

    @Test
    void testTransfer_senderNotActive() {
        // Arrange
        String fromId = "ACC1111";
        String toId = "ACC2222";
        Double amount = 100.0;
        String idempotencyKey = "key123";

        Account fromAccount = new Account();
        fromAccount.setAccountId(fromId);
        fromAccount.setStatus(AccountStatus.LOCKED); // or anything not ACTIVE

        Account toAccount = new Account();
        toAccount.setAccountId(toId);
        toAccount.setStatus(AccountStatus.ACTIVE);

        when(accountRepository.findByAccountId(fromId)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findByAccountId(toId)).thenReturn(Optional.of(toAccount));

        TransactionResponse response = transferService.transfer(fromId, toId, amount, idempotencyKey);
        assertEquals("FAILED", response.getStatus()); // or TransactionStatus.FAILED.name()
    }
    
    @Test
    void testTransfer_insufficientBalance() {
        // Arrange
        String fromId = "ACC1111";
        String toId = "ACC2222";
        Double amount = 1000.0; // greater than balance
        String idempotencyKey = "key123";

        Account fromAccount = new Account();
        fromAccount.setAccountId(fromId);
        fromAccount.setBalance(500.0);
        fromAccount.setStatus(AccountStatus.ACTIVE);

        Account toAccount = new Account();
        toAccount.setAccountId(toId);
        toAccount.setStatus(AccountStatus.ACTIVE);

        when(accountRepository.findByAccountId(fromId)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findByAccountId(toId)).thenReturn(Optional.of(toAccount));
        when(transactionLogRepository.findByIdempotencyKey(idempotencyKey)).thenReturn(null);

        // Act
        TransactionResponse response = transferService.transfer(fromId, toId, amount, idempotencyKey);

        // Assert
        assertEquals("FAILED", response.getStatus());
        verify(transactionLogRepository, times(1)).save(any(TransactionLog.class));
    }
    
    @Test
    void testTransfer_duplicateKey() {
        // Arrange
        String fromId = "ACC1111";
        String toId = "ACC2222";
        Double amount = 100.0;
        String idempotencyKey = "key123";

        Account fromAccount = new Account();
        fromAccount.setAccountId(fromId);
        fromAccount.setStatus(AccountStatus.ACTIVE);
        fromAccount.setBalance(1000.0);

        Account toAccount = new Account();
        toAccount.setAccountId(toId);
        toAccount.setStatus(AccountStatus.ACTIVE);

        when(accountRepository.findByAccountId(fromId)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findByAccountId(toId)).thenReturn(Optional.of(toAccount));
        when(transactionLogRepository.findByIdempotencyKey(idempotencyKey)).thenReturn(new TransactionLog()); // Exists

        // Act
        TransactionResponse response = transferService.transfer(fromId, toId, amount, idempotencyKey);

        // Assert
        assertEquals("FAILED", response.getStatus());
    }
}
