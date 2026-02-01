package com.money_transfer.backend.unit_tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    private Account account;

    @BeforeEach
    void setUp() {
        // Setup a sample account before each test
        account = new Account("A001", "John Doe", 1000.0, AccountStatus.ACTIVE, 1);
    }

    // Test case for successful debit
    @Test
    void testDebit_Success() {
        // Arrange
        double debitAmount = 500.0;

        // Act
        account.debit(debitAmount);

        // Assert
        assertEquals(500.0, account.getBalance(), "Balance should be 500 after debit.");
    }

    // Test case for debit failure due to insufficient balance
    @Test
    void testDebit_InsufficientBalance() {
        // Arrange
        double debitAmount = 1500.0; // More than the balance

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            account.debit(debitAmount);
        });

        assertEquals("Insufficient funds or account is not active.", exception.getMessage());
    }

    // Test case for successful credit
    @Test
    void testCredit_Success() {
        // Arrange
        double creditAmount = 200.0;

        // Act
        account.credit(creditAmount);

        // Assert
        assertEquals(1200.0, account.getBalance(), "Balance should be 1200 after credit.");
    }
}
