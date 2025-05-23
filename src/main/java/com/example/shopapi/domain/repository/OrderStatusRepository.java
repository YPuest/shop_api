package com.example.shopapi.domain.repository;

import com.example.shopapi.domain.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderStatusRepository extends JpaRepository<OrderStatus, Long> {
    Optional<OrderStatus> findByStatus(String status);
}