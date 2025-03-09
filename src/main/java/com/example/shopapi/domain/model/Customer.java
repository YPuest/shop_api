package com.example.shopapi.domain.model;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    protected Customer() {}

    public Customer(String name, String email) {
        this.name = name;
        this.email = email;
    }
}