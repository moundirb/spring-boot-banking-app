package com.academy.banking_app.service.impl;

import com.academy.banking_app.dto.*;
import com.academy.banking_app.entity.Customer;
import com.academy.banking_app.entity.Merchant;
import com.academy.banking_app.entity.User;
import com.academy.banking_app.repository.UserRepository;
import com.academy.banking_app.service.EmailService;
import com.academy.banking_app.service.UserService;
import com.academy.banking_app.utils.AccountUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
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
                                    .accountName(getFullName(existingUser))
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
        sendEmailAlert(savedUser,null);

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATED_CODE)
                .responseMessage(AccountUtils.ACCOUNT_CREATED_MESSAGE)

                .accountInfo(
                        AccountInfo.builder()
                                .accountBalance(savedUser.getAccountBalance())
                                .accountNumber(savedUser.getAccountNumber())
                                .accountName(getFullName(savedUser))
                                .build())
                .build();

    }
    // helper method to get full name
    private String getFullName(User user) {
        StringBuilder name = new StringBuilder();
        if (user.getFirstname() != null)  name.append(user.getFirstname());
        if (user.getLastname() != null)  name.append(" ").append(user.getLastname());
        if (user.getOthername() != null) name.append(" ").append(user.getOthername());
        return name.toString();
    }
    // Helper method to send email
private void sendEmailAlert(User user, String sp) {
    String subject = (sp != null && !sp.isEmpty()) ? sp + " Account Created Successfully" : "Account Created Successfully";
    EmailDetails emailDetails = EmailDetails.builder()
            .recipient(user.getEmail())
            .subject(subject)
            .msgBody("Dear " + getFullName(user) + ",\n\n" +
                    "Your account has been successfully created with the following details:\n" +
                    "Account Number: " + user.getAccountNumber() + "\n" +
                    "Best regards,\n" +
                    " App Team")
            .build();
    emailService.sendEmail(emailDetails);
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

        User user = userOptional.get();
        updateUserDetails(user, userRequest);
        User updatedUser = userRepository.save(user);

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_UPDATED_CODE)
                .responseMessage(AccountUtils.ACCOUNT_UPDATED_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(updatedUser.getAccountBalance())
                        .accountNumber(updatedUser.getAccountNumber())
                        .accountName(getFullName(updatedUser))
                        .build())
                .build();
    }

    private void updateUserDetails(User user, UserRequest userRequest) {
        if (userRequest.getFirstname() != null) user.setFirstname(userRequest.getFirstname());
        if (userRequest.getLastname() != null) user.setLastname(userRequest.getLastname());
        if (userRequest.getOthername() != null) user.setOthername(userRequest.getOthername());
        if (userRequest.getGender() != null) user.setGender(userRequest.getGender());
        if (userRequest.getAddress() != null) user.setAddress(userRequest.getAddress());
        if (userRequest.getStateOfOrigin() != null) user.setStateOfOrigin(userRequest.getStateOfOrigin());
        if (userRequest.getEmail() != null) user.setEmail(userRequest.getEmail());
        if (userRequest.getPhoneNumber() != null) user.setPhoneNumber(userRequest.getPhoneNumber());
        if (userRequest.getAlterPhoneNumber() != null) user.setAlterPhoneNumber(userRequest.getAlterPhoneNumber());
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
// for the customer creation

    @Override
    public BankResponse createCustomer(CustomerRequest customerRequest) {
        if (userRepository.existsByEmail(customerRequest.getEmail())) {
            // Similar logic as before for existing user
            User existingUser = userRepository.findByEmail(customerRequest.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
                    .accountInfo(
                            AccountInfo.builder()
                                    .accountBalance(existingUser.getAccountBalance())
                                    .accountNumber(existingUser.getAccountNumber())
                                    .accountName(getFullName(existingUser))
                                    .build())
                    .build();
        }

        Customer newCustomer = Customer.builder()
                .firstname(customerRequest.getFirstname())
                .lastname(customerRequest.getLastname())
                .othername(customerRequest.getOthername())
                .address(customerRequest.getAddress())
                .gender(customerRequest.getGender())
                .stateOfOrigin(customerRequest.getStateOfOrigin())
                .accountNumber(AccountUtils.generateAccountNumber())
                .email(customerRequest.getEmail())
                .accountBalance(BigDecimal.ZERO)
                .phoneNumber(customerRequest.getPhoneNumber())
                .alterPhoneNumber(customerRequest.getAlterPhoneNumber())
                .customerReferenceNumber(customerRequest.getCustomerReferenceNumber())  // Specific
                .dateOfBirth(LocalDate.parse(customerRequest.getDateOfBirth()))  // Specific
                .status("active")
                .build();

        Customer savedCustomer = (Customer) userRepository.save(newCustomer);  // Cast if needed, but JPA handles
        // Email logic same as before
        // Send Email Alert
        sendEmailAlert(savedCustomer, "Customer");

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATED_CODE)
                .responseMessage(AccountUtils.ACCOUNT_CREATED_MESSAGE)
                .accountInfo(
                        AccountInfo.builder()
                                .accountBalance(savedCustomer.getAccountBalance())
                                .accountNumber(savedCustomer.getAccountNumber())
                                .accountName(savedCustomer.getFirstname() + " " + savedCustomer.getLastname() + " " + savedCustomer.getOthername())
                                .build())
                .build();
    }

    @Override
    public BankResponse createMerchant(MerchantRequest merchantRequest) {
        if (userRepository.existsByEmail(merchantRequest.getEmail())) {
            // Similar logic as before for existing user
            User existingUser = userRepository.findByEmail(merchantRequest.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
                    .accountInfo(
                            AccountInfo.builder()
                                    .accountBalance(existingUser.getAccountBalance())
                                    .accountNumber(existingUser.getAccountNumber())
                                    .accountName(getFullName(existingUser))
                                    .build())
                    .build();
        }

        Merchant newMerchant = Merchant.builder()
                .firstname(merchantRequest.getFirstname())
                .lastname(merchantRequest.getLastname())
                .othername(merchantRequest.getOthername())
                .address(merchantRequest.getAddress())
                .gender(merchantRequest.getGender())
                .stateOfOrigin(merchantRequest.getStateOfOrigin())
                .accountNumber(AccountUtils.generateAccountNumber())
                .email(merchantRequest.getEmail())
                .accountBalance(BigDecimal.ZERO)
                .phoneNumber(merchantRequest.getPhoneNumber())
                .alterPhoneNumber(merchantRequest.getAlterPhoneNumber())
                .businessName(merchantRequest.getBusinessName())  // Specific
                .businessRegistrationNumber(merchantRequest.getBusinessRegistrationNumber())  // Specific
                .status("active")
                .build();

        Merchant savedMerchant = (Merchant) userRepository.save(newMerchant);  // Cast if needed, but JPA handles

        // Email logic same as before
        // Send Email Alert
        sendEmailAlert(savedMerchant, "Merchant");

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATED_CODE)
                .responseMessage(AccountUtils.ACCOUNT_CREATED_MESSAGE)
                .accountInfo(
                        AccountInfo.builder()
                                .accountBalance(savedMerchant.getAccountBalance())
                                .accountNumber(savedMerchant.getAccountNumber())
                                .accountName(savedMerchant.getFirstname() + " " + savedMerchant.getLastname() + " " + savedMerchant.getOthername())
                                .build())
                .build();
    }

    @Override
    public BankResponse balanceEnquiry(EnquiryRequest request) {
            //check if the provided  account number exists
        if (!userRepository.existsByAccountNumber(request.getAccountNumber())) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_FOUND_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_FOUND_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User foundUser = userRepository.findByAccountNumber(request.getAccountNumber())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_FOUND_CODE)
                .responseMessage(AccountUtils.ACCOUNT_FOUND_MESSAGE)
                .accountInfo(
                        AccountInfo.builder()
                                .accountBalance(foundUser.getAccountBalance())
                                .accountNumber(foundUser.getAccountNumber())
                                .accountName(getFullName(foundUser))
                                .build()
                )
                .build();

    }

    @Override
    public String nameEnquiry(EnquiryRequest request) {
        //check if the provided  account number exists
        if (!userRepository.existsByAccountNumber(request.getAccountNumber())) {
            return AccountUtils.ACCOUNT_NOT_FOUND_MESSAGE;
        }
        User foundUser = userRepository.findByAccountNumber(request.getAccountNumber())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return getFullName(foundUser);
    }

    @Override
    public BankResponse creditAccount(TransactionRequest request) {
        if (!userRepository.existsByAccountNumber(request.getAccountNumber())) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_FOUND_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_FOUND_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User creditedUser = userRepository.findByAccountNumber(request.getAccountNumber())
                .orElseThrow(() -> new RuntimeException("User not found"));
        // check if the amount is valid
        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.INVALID_AMOUNT_CODE)
                    .responseMessage(AccountUtils.INVALID_AMOUNT_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        // check if the account is active
        if (!creditedUser.getStatus().equalsIgnoreCase("active")) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_INACTIVE_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_INACTIVE_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        // credit the account
        creditedUser.setAccountBalance(creditedUser.getAccountBalance().add(request.getAmount()));
        userRepository.save(creditedUser);
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREDITED_CODE)
                .responseMessage(AccountUtils.ACCOUNT_CREDITED_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(creditedUser.getAccountBalance())
                        .accountNumber(creditedUser.getAccountNumber())
                        .accountName(getFullName(creditedUser))
                        .build())
                .build();

    }

    @Override
    public BankResponse debitAccount(TransactionRequest request) {
        if (!userRepository.existsByAccountNumber(request.getAccountNumber())) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_FOUND_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_FOUND_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User debitedUser = userRepository.findByAccountNumber(request.getAccountNumber())
                .orElseThrow(() -> new RuntimeException("User not found"));
        // check if the amount is valid
        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.INVALID_AMOUNT_CODE)
                    .responseMessage(AccountUtils.INVALID_AMOUNT_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        // check if the account is active
        if (!debitedUser.getStatus().equalsIgnoreCase("active")) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_INACTIVE_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_INACTIVE_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        // check if the account has sufficient balance
        if (debitedUser.getAccountBalance().compareTo(request.getAmount()) < 0) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        // debit the account
        debitedUser.setAccountBalance(debitedUser.getAccountBalance().subtract(request.getAmount()));
        userRepository.save(debitedUser);
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_DEBITED_CODE)
                .responseMessage(AccountUtils.ACCOUNT_DEBITED_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(debitedUser.getAccountBalance())
                        .accountNumber(debitedUser.getAccountNumber())
                        .accountName(getFullName(debitedUser))
                        .build())
                .build();

    }

    // balance Enquiry name Enquiry , credit, debit, transfer
}
