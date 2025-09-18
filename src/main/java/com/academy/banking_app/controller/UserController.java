package com.academy.banking_app.controller;

import com.academy.banking_app.dto.*;
import com.academy.banking_app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {


    private final UserService userService;


    // Endpoint to create a new user account
    @PostMapping("/createAccount")
    public BankResponse createAccount(@RequestBody UserRequest userRequest){
        return userService.createAccount(userRequest);
    }

    // get user by account number
    @GetMapping("/{accountNumber}")
    public BankResponse getAccountByAccountNumber(@PathVariable String accountNumber){
        return userService.getAccountByAccountNumber(accountNumber);
    }

    // update user account
    @PutMapping("/{accountNumber}")
    public BankResponse updateAccount(@PathVariable String accountNumber, @RequestBody UserRequest userRequest){
        return userService.updateAccount(accountNumber, userRequest);
    }
    // delete user account
    @DeleteMapping("/{accountNumber}")
    public BankResponse deleteAccount(@PathVariable String accountNumber){
        return userService.deleteAccount(accountNumber);
    }
    // create customer account

    @PostMapping("/createCustomer")
    public BankResponse createCustomer(@RequestBody CustomerRequest customerRequest) {
        return userService.createCustomer(customerRequest);
    }

    @PostMapping("/createMerchant")
    public BankResponse createMerchant(@RequestBody MerchantRequest merchantRequest) {
        return userService.createMerchant(merchantRequest);
    }

// Existing endpoints can handle updates/deletes by accountNumber, as they load User (which could be subclass)

    @GetMapping("/balanceEnquiry")
    public BankResponse balanceEnquiry(@RequestBody EnquiryRequest request) {
        return userService.balanceEnquiry(request);
    }
    @GetMapping("/nameEnquiry")
    public String nameEnquiry(@RequestBody EnquiryRequest request) {
        return userService.nameEnquiry(request);
    }
    @PostMapping("/creditAccount")
    public BankResponse creditAccount(@RequestBody TransactionRequest request) {
        return userService.creditAccount(request);
    }
    @PostMapping("/debitAccount")
    public BankResponse debitAccount(@RequestBody TransactionRequest request) {
        return userService.debitAccount(request);
    }

}
