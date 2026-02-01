package com.money_transfer.backend.unit_tests;

import org.junit.jupiter.api.Test;
import javax.validation.*;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class TransferRequestValidationTest {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    // Test case for a valid request
    @Test
    void testValidRequest() {
        TransferRequest request = new TransferRequest("A001", "A002", 100.0);

        Set<ConstraintViolation<TransferRequest>> violations = validator.validate(request);

        // Assert that there are no violations for a valid request
        assertTrue(violations.isEmpty(), "There should be no validation errors.");
    }

    // Test case for invalid amount (should be greater than 0)
    @Test
    void testInvalidAmount() {
        TransferRequest request = new TransferRequest("A001", "A002", -10.0);  // Invalid amount

        Set<ConstraintViolation<TransferRequest>> violations = validator.validate(request);

        // Assert that the amount validation fails
        assertFalse(violations.isEmpty(), "Amount should be greater than zero.");
        assertEquals("Amount must be greater than zero", violations.iterator().next().getMessage());
    }

    // Test case for null fields
    @Test
    void testNullFields() {
        // Test case when "fromAccountId" is null
        TransferRequest request1 = new TransferRequest(null, "A002", 100.0);
        Set<ConstraintViolation<TransferRequest>> violations1 = validator.validate(request1);
        assertFalse(violations1.isEmpty(), "From Account ID cannot be null.");

        // Test case when "toAccountId" is null
        TransferRequest request2 = new TransferRequest("A001", null, 100.0);
        Set<ConstraintViolation<TransferRequest>> violations2 = validator.validate(request2);
        assertFalse(violations2.isEmpty(), "To Account ID cannot be null.");

        // Test case when "amount" is null
        TransferRequest request3 = new TransferRequest("A001", "A002", null);
        Set<ConstraintViolation<TransferRequest>> violations3 = validator.validate(request3);
        assertFalse(violations3.isEmpty(), "Amount cannot be null.");
    }
}
