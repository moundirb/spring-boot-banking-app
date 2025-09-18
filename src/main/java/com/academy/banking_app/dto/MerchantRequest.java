package com.academy.banking_app.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class MerchantRequest extends UserRequest {
    private String businessName; // Name of the business
    private String businessRegistrationNumber; // Official registration number of the business
}