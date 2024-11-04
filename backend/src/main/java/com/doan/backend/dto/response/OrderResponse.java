package com.doan.backend.dto.response;

import com.doan.backend.entity.OrderItem;
import com.doan.backend.entity.ShippingAddress;
import com.doan.backend.entity.User;
import com.doan.backend.entity.UserDiscount;
import com.doan.backend.enums.OrderStatusEnum;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
@AllArgsConstructor
public class OrderResponse {
    String id;

    UserDiscount userDiscount;

    ShippingAddress shippingAddress;

    OrderStatusEnum status;

    BigDecimal totalPriceBeforeDiscount;

    BigDecimal totalPriceAfterDiscount;

    List<OrderItem> orderItems;

    LocalDateTime createdAt;

    LocalDateTime updatedAt;
}
