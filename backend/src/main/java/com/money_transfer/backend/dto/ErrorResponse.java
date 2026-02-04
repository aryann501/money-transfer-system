package com.money_transfer.backend.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ErrorResponse {

    private String errorCode;
    private String errorMessage;
}

