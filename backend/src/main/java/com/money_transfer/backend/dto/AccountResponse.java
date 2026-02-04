package com.money_transfer.backend.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AccountResponse {

    private Integer id;
    private String holderName;
    private Double balance;
    private String status;
}

