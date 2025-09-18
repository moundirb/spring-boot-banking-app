package com.academy.banking_app.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "merchants")
@DiscriminatorValue("MERCHANT")
public class Merchant extends User {

    private String businessName;
    private String businessRegistrationNumber;


}
