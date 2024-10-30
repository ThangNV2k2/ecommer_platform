package com.doan.backend.repositories;

import com.doan.backend.entity.Order;
import com.doan.backend.enums.OrderStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    Iterable<Order> findByUserId(String userId);
    Iterable<Order> findByStatus(OrderStatusEnum status);
    Iterable<Order> findByUserIdAndStatus(String userId, OrderStatusEnum status);
    Boolean existsByUserIdAndStatus(String userId, OrderStatusEnum status);
}
