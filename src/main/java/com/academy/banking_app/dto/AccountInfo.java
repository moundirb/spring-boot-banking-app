package com.academy.banking_app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class AccountInfo
{
    private String accountNumber;
    private String accountName;
    private BigDecimal accountBalance;

}
