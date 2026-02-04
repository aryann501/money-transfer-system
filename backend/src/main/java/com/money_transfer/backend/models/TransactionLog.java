package com.money_transfer.backend.models;



import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class TransactionLog {

    private Integer id;
    private Integer fromAccountId;
    private Integer toAccountId;
    private Double amount;
    private TransactionStatus status;
    private String failureReason;
    private String idempotencyKey;
    private LocalDateTime createdOn;
}
