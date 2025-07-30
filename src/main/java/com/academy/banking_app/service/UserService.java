package com.academy.banking_app.service;

import com.academy.banking_app.dto.BankResponse;
import com.academy.banking_app.dto.UserRequest;

public interface UserService {

    BankResponse createAccount(UserRequest userRequest);
    BankResponse getAccountByAccountNumber(String accountNumber);
    BankResponse updateAccount(String accountNumber, UserRequest userRequest);
    BankResponse updatePartialAccount(String accountNumber, UserRequest userRequest);
    BankResponse deleteAccount(String accountNumber);

}
