package com.example.backend.unittest;

import com.example.backend.controllers.AccountController;
import com.example.backend.dtos.AccountDTO;
import com.example.backend.dtos.TransactionResponse;
<<<<<<< HEAD
import com.example.backend.exceptions.AccountNotFoundException;
=======
>>>>>>> f8e4805 (Added proper unit Test cases with 93% code coverage from Jacoco)
import com.example.backend.security.service.UserDetailsImpl;
import com.example.backend.services.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
<<<<<<< HEAD
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
=======
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
>>>>>>> f8e4805 (Added proper unit Test cases with 93% code coverage from Jacoco)

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountControllerTest {

    @InjectMocks
    private AccountController accountController;

    @Mock
    private AccountService accountService;

<<<<<<< HEAD
    private UserDetailsImpl userDetails;

=======
>>>>>>> f8e4805 (Added proper unit Test cases with 93% code coverage from Jacoco)
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

<<<<<<< HEAD
    // --- ADMIN ENDPOINT TESTS ---

    @Test
    void testGetAccount_admin_success() {
        AccountDTO account = new AccountDTO();
        account.setAccountId("123");
        account.setBalance(1000.0);

        when(accountService.getAccount("123")).thenReturn(account);

        ResponseEntity<?> response = accountController.getAccount("123");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(account, response.getBody());
    }

    @Test
    void testGetAccount_admin_notFound() {
        when(accountService.getAccount("999")).thenThrow(new AccountNotFoundException("Account not found"));

        assertThrows(AccountNotFoundException.class, () -> accountController.getAccount("999"));
    }

    @Test
    void testGetTransactionsById_success() throws AccountNotFoundException {
        TransactionResponse tx = new TransactionResponse();
        tx.setFromAccountId("123");
        tx.setToAccountId("456");
        tx.setAmount(100.0);

        List<TransactionResponse> transactions = Arrays.asList(tx);

        when(accountService.getTransactions("123")).thenReturn(transactions);

        ResponseEntity<List<TransactionResponse>> response = accountController.getTransactionsById("123");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(transactions, response.getBody());
    }

    @Test
    void testGetTransactionsById_notFound() throws AccountNotFoundException {
        when(accountService.getTransactions("999")).thenThrow(new AccountNotFoundException("Account not found"));

        assertThrows(AccountNotFoundException.class, () -> accountController.getTransactionsById("999"));
    }

    // --- USER ENDPOINT TESTS ---

    @Test
    void testGetMyAccount_success() {
        AccountDTO account = new AccountDTO();
        account.setAccountId("user123");
        account.setBalance(200.0);

        userDetails = new UserDetailsImpl(
                1L, "user", "pass",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")),
                "user123"
        );
        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken(userDetails, null));

        when(accountService.getAccount("user123")).thenReturn(account);

        ResponseEntity<?> response = accountController.getMyAccount();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(account, response.getBody());
    }

    @Test
    void testGetMyAccount_notFound() {
        userDetails = new UserDetailsImpl(
                1L, "user", "pass",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")),
                "user999"
        );
        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken(userDetails, null));

        when(accountService.getAccount("user999")).thenThrow(new AccountNotFoundException("Account not found"));

        assertThrows(AccountNotFoundException.class, () -> accountController.getMyAccount());
=======
    @Test
    void testGetAccount_success() {
        // Arrange
        String accountId = "ACC123";
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setAccountId(accountId);
        
        when(accountService.getAccount(accountId)).thenReturn(accountDTO);

        // Act
        ResponseEntity<?> response = accountController.getAccount(accountId);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertEquals(accountDTO, response.getBody());
>>>>>>> f8e4805 (Added proper unit Test cases with 93% code coverage from Jacoco)
    }

    @Test
    void testGetMyBalance_success() {
<<<<<<< HEAD
        AccountDTO account = new AccountDTO();
        account.setAccountId("user123");
        account.setBalance(300.0);

        userDetails = new UserDetailsImpl(
                1L, "user", "pass",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")),
                "user123"
        );
        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken(userDetails, null));

        when(accountService.getAccount("user123")).thenReturn(account);

        ResponseEntity<?> response = accountController.getMyBalance();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(300.0, response.getBody());
    }

    @Test
    void testGetMyTransactions_success() throws AccountNotFoundException {
        TransactionResponse tx = new TransactionResponse();
        tx.setFromAccountId("user123");
        tx.setToAccountId("456");
        tx.setAmount(50.0);

        List<TransactionResponse> transactions = Arrays.asList(tx);

        userDetails = new UserDetailsImpl(
                1L, "user", "pass",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")),
                "user123"
        );
        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken(userDetails, null));

        when(accountService.getTransactions("user123")).thenReturn(transactions);

        ResponseEntity<List<TransactionResponse>> response = accountController.getMyTransactions();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(transactions, response.getBody());
    }

    @Test
    void testGetMyTransactions_notFound() throws AccountNotFoundException {
        userDetails = new UserDetailsImpl(
                1L, "user", "pass",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")),
                "user999"
        );
        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken(userDetails, null));

        when(accountService.getTransactions("user999")).thenThrow(new AccountNotFoundException("Account not found"));

        assertThrows(AccountNotFoundException.class, () -> accountController.getMyTransactions());
    }
=======
        // Arrange
        String accountId = "ACC123";
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        when(userDetails.getAccountId()).thenReturn(accountId);
        
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setBalance(500.0);
        
        when(accountService.getAccount(accountId)).thenReturn(accountDTO);

        // Act
        ResponseEntity<?> response = accountController.getMyBalance(userDetails);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertEquals(500.0, response.getBody());
    }

    @Test
    void testGetTransactions_success() {
        // Arrange
        String accountId = "ACC123";
        TransactionResponse tx = new TransactionResponse();
        tx.setAmount(100.0);
        
        when(accountService.getTransactions(accountId)).thenReturn(Collections.singletonList(tx));

        // Act
        ResponseEntity<?> response = accountController.getTransactions(accountId);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertInstanceOf(List.class, response.getBody());
        List<?> list = (List<?>) response.getBody();
        assertEquals(1, list.size());
    }
    
    // Note: PreAuthorize annotations are not tested in unit tests with @InjectMocks.
    // Use MockMvc with @WebMvcTest for security testing, but here we are doing unit testing of logic.
    // Auth logic is handled by Spring Security, unit tests verify controller delegates to service correctly.
>>>>>>> f8e4805 (Added proper unit Test cases with 93% code coverage from Jacoco)
}
