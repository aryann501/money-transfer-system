package com.money_transfer.backend.controller;

import com.money_transfer.backend.dto.TransferRequest;
import com.money_transfer.backend.dto.TransferResponse;
import com.money_transfer.backend.dto.ErrorResponse;
import com.money_transfer.backend.exceptions.*;
import com.money_transfer.backend.services.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transfers")
public class TransferController {

    @Autowired
    private TransferService transferService;

    @PostMapping
    public TransferResponse transfer(@RequestBody TransferRequest request) {
        return transferService.transfer(request);
    }

    // Exception handlers for transfer errors
    @ExceptionHandler(AccountNotFoundException.class)
    public ErrorResponse handleAccountNotFound(AccountNotFoundException ex) {
        ErrorResponse error = new ErrorResponse();
        error.setErrorCode("ACCOUNT_NOT_FOUND");
        error.setErrorMessage(ex.getMessage());
        return error;
    }

    @ExceptionHandler(AccountNotActiveException.class)
    public ErrorResponse handleAccountNotActive(AccountNotActiveException ex) {
        ErrorResponse error = new ErrorResponse();
        error.setErrorCode("ACCOUNT_NOT_ACTIVE");
        error.setErrorMessage(ex.getMessage());
        return error;
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ErrorResponse handleInsufficientBalance(InsufficientBalanceException ex) {
        ErrorResponse error = new ErrorResponse();
        error.setErrorCode("INSUFFICIENT_BALANCE");
        error.setErrorMessage(ex.getMessage());
        return error;
    }

    @ExceptionHandler(DuplicateTransferException.class)
    public ErrorResponse handleDuplicateTransfer(DuplicateTransferException ex) {
        ErrorResponse error = new ErrorResponse();
        error.setErrorCode("DUPLICATE_TRANSFER");
        error.setErrorMessage(ex.getMessage());
        return error;
    }
}
