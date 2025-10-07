package com.example.rewards.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder                  
public class Customer {

    @Id                      
    @GeneratedValue(strategy = GenerationType.IDENTITY)      
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
}
