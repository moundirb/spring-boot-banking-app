package com.academy.banking_app.service.impl;

import com.academy.banking_app.dto.AccountInfo;
import com.academy.banking_app.dto.BankResponse;
import com.academy.banking_app.dto.EmailDetails;
import com.academy.banking_app.dto.UserRequest;
import com.academy.banking_app.entity.User;
import com.academy.banking_app.repository.UserRepository;
import com.academy.banking_app.service.EmailService;
import com.academy.banking_app.service.UserService;
import com.academy.banking_app.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    /**
     * UserServiceImpl is a service class that implements the UserService interface.
     * It provides methods for creating, updating, retrieving, and deleting user accounts.
     * It also handles email notifications for account creation.
     */

    private final EmailService emailService;

    private final UserRepository userRepository;


    public UserServiceImpl(EmailService emailService, UserRepository userRepository) {
        this.emailService = emailService;
        this.userRepository = userRepository;
    }

    /**
     * Account creation - saving a new user in the database.
     * - Check if the user already exists by email.
     * - Retrieve the user by email.
     * - Update the user if they already exist.
     * - Partially update the user if they already exist.
     * - Delete the user if they already exist.
     */


    @Override
    public BankResponse createAccount(UserRequest userRequest) {

        if (userRepository.existsByEmail(userRequest.getEmail())) {

            User existingUser = userRepository.findByEmail(userRequest.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // If the user already exists, return a response indicating that the account already exists

            // You can also return the existing user's account information if needed

            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
                    .accountInfo(
                            AccountInfo.builder()
                                    .accountBalance(existingUser.getAccountBalance())
                                    .accountNumber(existingUser.getAccountNumber())
                                    .accountName(existingUser.getFirstname() + " " + existingUser.getLastname() + " " + existingUser.getOthername())
                                    .build()
                    )
                    .build();

        }

        User newUser = User.builder()
                .firstname(userRequest.getFirstname())
                .lastname(userRequest.getLastname())
                .othername(userRequest.getOthername())
                .address(userRequest.getAddress())
                .gender(userRequest.getGender())
                .stateOfOrigin(userRequest.getStateOfOrigin())
                .accountNumber(AccountUtils.generateAccountNumber())
                .email(userRequest.getEmail())
                .accountBalance(BigDecimal.ZERO)
                .phoneNumber(userRequest.getPhoneNumber())
                .alterPhoneNumber(userRequest.getAlterPhoneNumber())
                .status("active")
                .build();

        User savedUser = userRepository.save(newUser);

        // Send Email Alert
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(savedUser.getEmail())
                .subject("Account Created Successfully")
                .msgBody("Dear " + savedUser.getFirstname() + savedUser.getLastname() + savedUser.getOthername() + "\n\n"+
                        "Your account has been successfully created with the following details:\n" +
                        "Account Number: " + savedUser.getAccountNumber() + "\n" +
                        "Best regards,\n" +
                        " App Team")
                .build();
        emailService.sendEmail(emailDetails);

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATED_CODE)
                .responseMessage(AccountUtils.ACCOUNT_CREATED_MESSAGE)

                .accountInfo(
                        AccountInfo.builder()
                                .accountBalance(savedUser.getAccountBalance())
                                .accountNumber(savedUser.getAccountNumber())
                                .accountName(savedUser.getFirstname() + " " + savedUser.getLastname() + " " + savedUser.getOthername())
                                .build())
                .build();

    }

    // Method to get account information by account number
    @Override
    public BankResponse getAccountByAccountNumber(String accountNumber) {
        Optional<User> userOptional = userRepository.findByAccountNumber(accountNumber);
        if (userOptional.isEmpty()) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_FOUND_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_FOUND_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        User user = userOptional.get();
        AccountInfo accountInfo = AccountInfo.builder()
                .accountBalance(user.getAccountBalance())
                .accountNumber(user.getAccountNumber())
                .accountName(user.getFirstname() + " " + user.getLastname() + " " + user.getOthername())
                .build();

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_FOUND_CODE)
                .responseMessage(AccountUtils.ACCOUNT_FOUND_MESSAGE)
                .accountInfo(accountInfo)
                .build();
    }

    // Method to update account information
    @Override
    public BankResponse updateAccount(String accountNumber, UserRequest userRequest) {
        Optional<User> userOptional = userRepository.findByAccountNumber(accountNumber);
        if (userOptional.isEmpty()) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_FOUND_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_FOUND_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User user = getUser(userRequest, userOptional);

        User updatedUser = userRepository.save(user);

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_UPDATED_CODE)
                .responseMessage(AccountUtils.ACCOUNT_UPDATED_MESSAGE)
                .accountInfo(
                        AccountInfo.builder()
                                .accountBalance(updatedUser.getAccountBalance())
                                .accountNumber(updatedUser.getAccountNumber())
                                .accountName(updatedUser.getFirstname() + " " + updatedUser.getLastname() + " " + updatedUser.getOthername())
                                .build()
                )
                .build();
    }

    private static User getUser(UserRequest userRequest, Optional<User> userOptional) {
        User user = userOptional.get();
        // Update user details
        user.setFirstname(userRequest.getFirstname());
        user.setLastname(userRequest.getLastname());
        user.setOthername(userRequest.getOthername());
        user.setGender(userRequest.getGender());
        user.setAddress(userRequest.getAddress());
        user.setStateOfOrigin(userRequest.getStateOfOrigin());
        user.setEmail(userRequest.getEmail());
        user.setPhoneNumber(userRequest.getPhoneNumber());
        user.setAlterPhoneNumber(userRequest.getAlterPhoneNumber());
        return user;
    }

    // Method to update account information Partially

    @Override
    public BankResponse updatePartialAccount(String accountNumber, UserRequest userRequest) {
        Optional<User> userOptional = userRepository.findByAccountNumber(accountNumber);
        if (userOptional.isEmpty()) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_FOUND_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_FOUND_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User user = userOptional.get();
        applyPartialUpdate(user, userRequest);

        User updatedUser = userRepository.save(user);

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_UPDATED_CODE)
                .responseMessage(AccountUtils.ACCOUNT_UPDATED_MESSAGE)
                .accountInfo(
                        AccountInfo.builder()
                                .accountBalance(updatedUser.getAccountBalance())
                                .accountNumber(updatedUser.getAccountNumber())
                                .accountName(updatedUser.getFirstname() + " " + updatedUser.getLastname() + " " + updatedUser.getOthername())
                                .build()
                )
                .build();
    }

        /**
         * Applies partial updates to the User entity based on the provided UserRequest.
         *
         * @param user        The User entity to be updated.
         * @param userRequest The UserRequest containing the fields to update.
         */
        private void applyPartialUpdate(User user, UserRequest userRequest) {


            if (userRequest.getFirstname() != null) {
                user.setFirstname(userRequest.getFirstname());
            }
            if (userRequest.getLastname() != null) {
                user.setLastname(userRequest.getLastname());
            }
            if (userRequest.getOthername() != null) {
                user.setOthername(userRequest.getOthername());
            }
            if (userRequest.getEmail() != null) {
                user.setEmail(userRequest.getEmail());
            }
            if (userRequest.getPhoneNumber() != null) {
                user.setPhoneNumber(userRequest.getPhoneNumber());
            }
            if (userRequest.getAlterPhoneNumber() != null) {
                user.setAlterPhoneNumber(userRequest.getAlterPhoneNumber());
            }
            if (userRequest.getGender() != null) {
                user.setGender(userRequest.getGender());
            }
            if (userRequest.getAddress() != null) {
                user.setAddress(userRequest.getAddress());
            }
            if (userRequest.getStateOfOrigin() != null) {
                user.setStateOfOrigin(userRequest.getStateOfOrigin());
            }

        }


    // Method to delete an account by account number

    @Override
    public BankResponse deleteAccount(String accountNumber) {
        Optional<User> userOptional = userRepository.findByAccountNumber(accountNumber);
        if (userOptional.isEmpty()) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_FOUND_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_FOUND_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User user = userOptional.get();
        userRepository.delete(user);

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_DELETED_CODE)
                .responseMessage(AccountUtils.ACCOUNT_DELETED_MESSAGE)
                .accountInfo(null)
                .build();

    }
}
