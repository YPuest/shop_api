package com.example.shopapi.domain.repository;

import com.example.shopapi.domain.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}