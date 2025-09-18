package com.academy.banking_app.entity;


import com.academy.banking_app.enums.UserType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("USER")  // ✅ ADD THIS: Forces JPA to use "USER" for base User entities
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstname;
    private String lastname;
    private String othername;
    private String gender;
    private String address;
    private String stateOfOrigin;

    @Column(unique = true)
    private String accountNumber;

    private BigDecimal accountBalance;

    @Column(unique = true , nullable = false)
    private String email;

    private String phoneNumber;
    private String alterPhoneNumber;
    private String status;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type", insertable = false, updatable = false)
    private UserType userType;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
