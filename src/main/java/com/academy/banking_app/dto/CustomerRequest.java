package com.academy.banking_app.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CustomerRequest extends UserRequest {
    private String customerReferenceNumber;
    private String dateOfBirth; // Format: YYYY-MM-DD
}
