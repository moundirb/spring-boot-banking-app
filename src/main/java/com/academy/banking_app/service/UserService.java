package com.academy.banking_app.service;

import com.academy.banking_app.dto.*;

public interface UserService {

    BankResponse createAccount(UserRequest userRequest);
    BankResponse getAccountByAccountNumber(String accountNumber);
    BankResponse updateAccount(String accountNumber, UserRequest userRequest);
    BankResponse deleteAccount(String accountNumber);

    BankResponse createCustomer(CustomerRequest customerRequest);
    BankResponse createMerchant(MerchantRequest merchantRequest);

    BankResponse balanceEnquiry(EnquiryRequest request);
    String nameEnquiry(EnquiryRequest request);

    BankResponse creditAccount(TransactionRequest request);
    BankResponse debitAccount(TransactionRequest request);
}
