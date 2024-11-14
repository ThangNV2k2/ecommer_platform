package com.doan.backend.repositories;

import com.doan.backend.dto.response.ProductResponse;
import com.doan.backend.entity.OrderItem;
import com.doan.backend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, String> {
    Iterable<OrderItem> findByOrderId(String orderId);
}
