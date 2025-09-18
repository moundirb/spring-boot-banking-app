package com.academy.banking_app.repository;

import com.academy.banking_app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);
    boolean existsByAccountNumber(String accountNumber);

    Optional<User> findByEmail(String email);

    Optional<User> findByAccountNumber(String accountNumber);


}
