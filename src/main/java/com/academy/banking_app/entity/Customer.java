package com.academy.banking_app.entity;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "customers")
@DiscriminatorValue("CUSTOMER")
public class Customer extends User{
    private String customerReferenceNumber;
    private LocalDate dateOfBirth;


}
