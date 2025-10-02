package com.example.rewards.repository;

import com.example.rewards.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
//Auto provides methods like findById, save, delete