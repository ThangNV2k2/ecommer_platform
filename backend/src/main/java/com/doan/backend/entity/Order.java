package com.doan.backend.entity;

import com.doan.backend.enums.OrderStatusEnum;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne
    @JoinColumn(name = "discount_id")
    Discount discount;

    @ManyToOne
    @JoinColumn(name = "promotion_id")
    Promotion promotion;

    @ManyToOne
    @JoinColumn(name = "address_id", nullable = false)  // Liên kết đến bảng Address
    Address address;

    @ManyToOne
    @JoinColumn(name = "phone_number_id", nullable = false)  // Liên kết đến bảng PhoneNumber
    PhoneNumber phoneNumber;

    @Column(name = "status", nullable = false)
    OrderStatusEnum status;

    @Column(name = "total_price_before_discount")
    BigDecimal totalPriceBeforeDiscount;

    @Column(name = "total_price_after_discount")
    BigDecimal totalPriceAfterDiscount;

    @CreationTimestamp
    @Column(name = "created_at")
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    LocalDateTime updatedAt;
}
