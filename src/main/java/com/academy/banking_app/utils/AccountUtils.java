package com.academy.banking_app.utils;

import com.academy.banking_app.dto.UserRequest;
import com.academy.banking_app.entity.User;

import java.time.Year;

public class AccountUtils {




    public static final String ACCOUNT_EXISTS_CODE = "001";
    public static final String ACCOUNT_EXISTS_MESSAGE = "Account already exists";

    public static final String ACCOUNT_CREATED_CODE = "002";
    public static final String ACCOUNT_CREATED_MESSAGE = "Account created";

    public static final String ACCOUNT_FOUND_CODE = "003";
    public static final String ACCOUNT_FOUND_MESSAGE = "Account found";

    public static final String ACCOUNT_NOT_FOUND_CODE = "004";
    public static final String ACCOUNT_NOT_FOUND_MESSAGE = "Account not found";

    public static final String ACCOUNT_UPDATED_CODE = "006";
    public static final String ACCOUNT_UPDATED_MESSAGE = "Account updated";

    public static final String ACCOUNT_DELETED_CODE = "005";
    public static final String ACCOUNT_DELETED_MESSAGE = "Account deleted";
    



    /**
     * 2025 + randomSixDigits
     */

    static Year CurrentYear = Year.now();
    static int min = 100000;
    static int max = 999999;
    public static String generateAccountNumber(){
        int randomSixDigits = (int) Math.floor(Math.random() * (max -min  + 1) + min);
        return  String.valueOf(CurrentYear) + randomSixDigits;
    }




}
