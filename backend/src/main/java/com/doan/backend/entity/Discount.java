package com.doan.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "discounts")
public class Discount {

    @Id
    @GeneratedValue
    UUID id;

    @Column(name = "code", unique = true, nullable = false)
    String code;

    @Column(name = "discount_percentage", nullable = false)
    BigDecimal discountPercentage;

    @Column(name = "max_uses", nullable = false)
    Integer maxUses;

    @Column(name = "expiry_date")
    LocalDateTime expiryDate;

    @Column(name = "created_at")
    LocalDateTime createdAt;
}
