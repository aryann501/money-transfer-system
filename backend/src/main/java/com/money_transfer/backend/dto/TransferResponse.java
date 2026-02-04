package com.money_transfer.backend.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TransferResponse {

    private String message;
    private Integer transactionId;
    private String status;
}
