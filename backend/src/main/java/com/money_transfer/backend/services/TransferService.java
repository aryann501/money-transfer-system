package com.money_transfer.backend.services;



import com.money_transfer.backend.dto.TransferResponse;
import com.money_transfer.backend.dto.TransferRequest;
import com.money_transfer.backend.exceptions.*;

public interface TransferService {
    TransferResponse transfer(TransferRequest request)
            throws AccountNotFoundException, AccountNotActiveException,
            InsufficientBalanceException, DuplicateTransferException;
}
