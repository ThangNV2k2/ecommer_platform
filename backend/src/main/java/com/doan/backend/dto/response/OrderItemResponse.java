package com.doan.backend.dto.response;

import com.doan.backend.entity.Order;
import com.doan.backend.entity.Product;
import com.doan.backend.entity.Promotion;
import com.doan.backend.entity.Size;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
@AllArgsConstructor
public class OrderItemResponse {
    String id;

    ProductResponse productResponse;

    Size size;

    Integer quantity;

    Promotion promotion;

    BigDecimal price;
}
