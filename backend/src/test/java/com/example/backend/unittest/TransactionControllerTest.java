package com.example.backend.unittest;

import com.example.backend.controllers.TransactionController;
<<<<<<< HEAD
import com.example.backend.dtos.UserTransferRequest;
import com.example.backend.dtos.TransactionResponse;
import com.example.backend.exceptions.*;
import com.example.backend.security.service.UserDetailsImpl;
=======
import com.example.backend.dtos.TransactionResponse;
import com.example.backend.enums.TransactionStatus;
>>>>>>> f8e4805 (Added proper unit Test cases with 93% code coverage from Jacoco)
import com.example.backend.services.TransferService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
<<<<<<< HEAD
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
=======
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
>>>>>>> f8e4805 (Added proper unit Test cases with 93% code coverage from Jacoco)

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionControllerTest {

    @InjectMocks
    private TransactionController transactionController;

    @Mock
    private TransferService transferService;

<<<<<<< HEAD
    private UserDetailsImpl userDetails;

=======
>>>>>>> f8e4805 (Added proper unit Test cases with 93% code coverage from Jacoco)
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

<<<<<<< HEAD
    // --- USER TRANSFER TESTS ---

    @Test
    void testTransfer_user_success() throws Exception {
        userDetails = new UserDetailsImpl(
                1L, "user", "pass",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")),
                "user123"
        );
        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken(userDetails, null));

        UserTransferRequest request = new UserTransferRequest();
        request.setToAccountId("456");
        request.setAmount(100.0);
        request.setIdempotencyKey("key123");

        TransactionResponse txResponse = new TransactionResponse();
        txResponse.setFromAccountId("user123");
        txResponse.setToAccountId("456");
        txResponse.setAmount(100.0);
        txResponse.setStatus("SUCCESS");

        when(transferService.transfer("user123", "456", 100.0, "key123")).thenReturn(txResponse);

        ResponseEntity<TransactionResponse> response = transactionController.transferAsUser(request);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(txResponse, response.getBody());
    }

    @Test
    void testTransfer_user_accountNotFound() throws Exception {
        setUserContext("user123");

        UserTransferRequest request = new UserTransferRequest();
        request.setToAccountId("999");
        request.setAmount(100.0);
        request.setIdempotencyKey("key123");

        when(transferService.transfer("user123", "999", 100.0, "key123"))
                .thenThrow(new AccountNotFoundException("Receiver account not found"));

        assertThrows(AccountNotFoundException.class,
                () -> transactionController.transferAsUser(request));
    }

    @Test
    void testTransfer_user_accountNotActive() throws Exception {
        setUserContext("user123");

        UserTransferRequest request = new UserTransferRequest();
        request.setToAccountId("456");
        request.setAmount(100.0);
        request.setIdempotencyKey("key123");

        when(transferService.transfer("user123", "456", 100.0, "key123"))
                .thenThrow(new AccountNotActiveException("Sender account is not active"));

        assertThrows(AccountNotActiveException.class,
                () -> transactionController.transferAsUser(request));
    }

    @Test
    void testTransfer_user_insufficientBalance() throws Exception {
        setUserContext("user123");

        UserTransferRequest request = new UserTransferRequest();
        request.setToAccountId("456");
        request.setAmount(1000.0);
        request.setIdempotencyKey("key123");

        when(transferService.transfer("user123", "456", 1000.0, "key123"))
                .thenThrow(new InsufficientBalanceException("Insufficient balance"));

        assertThrows(InsufficientBalanceException.class,
                () -> transactionController.transferAsUser(request));
    }

    @Test
    void testTransfer_user_duplicateTransfer() throws Exception {
        setUserContext("user123");

        UserTransferRequest request = new UserTransferRequest();
        request.setToAccountId("456");
        request.setAmount(100.0);
        request.setIdempotencyKey("dupKey");

        when(transferService.transfer("user123", "456", 100.0, "dupKey"))
                .thenThrow(new DuplicateTransferException("Duplicate transfer detected"));

        assertThrows(DuplicateTransferException.class,
                () -> transactionController.transferAsUser(request));
    }

    // Helper to set user context
    private void setUserContext(String accountId) {
        userDetails = new UserDetailsImpl(
                1L, "user", "pass",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")),
                accountId
        );
        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken(userDetails, null));
=======
    @Test
    void testTransfer_success() throws Exception {
        // Arrange
        String from = "ACC1";
        String to = "ACC2";
        Double amount = 100.0;
        String key = "key1";
        
        TransactionResponse txResponse = new TransactionResponse();
        txResponse.setStatus(TransactionStatus.SUCCESS.name());
        txResponse.setAmount(amount);

        when(transferService.transfer(from, to, amount, key)).thenReturn(txResponse);

        // Act
        ResponseEntity<TransactionResponse> response = transactionController.transfer(from, to, amount, key);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(txResponse, response.getBody());
        assertNotNull(response.getBody());
        assertEquals("SUCCESS", response.getBody().getStatus());
>>>>>>> f8e4805 (Added proper unit Test cases with 93% code coverage from Jacoco)
    }
}
