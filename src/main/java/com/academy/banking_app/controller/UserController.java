package com.academy.banking_app.controller;

import com.academy.banking_app.dto.BankResponse;
import com.academy.banking_app.dto.UserRequest;
import com.academy.banking_app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {


    private final UserService userService;


    // Endpoint to create a new user account
    @PostMapping("/create-account")
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
    // update a user account partially

    @PatchMapping("/{accountNumber}")
    public BankResponse updatePartialAccount(
            @PathVariable String accountNumber,
            @RequestBody UserRequest userRequest) {
        return userService.updatePartialAccount(accountNumber, userRequest);
    }
    // delete user account
    @DeleteMapping("/{accountNumber}")
    public BankResponse deleteAccount(@PathVariable String accountNumber){
        return userService.deleteAccount(accountNumber);
    }


}
