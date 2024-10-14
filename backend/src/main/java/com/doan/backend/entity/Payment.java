package com.doan.backend.entity;

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
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    Order order;

    @Column(name = "payment_method", nullable = false)
    String paymentMethod;

    @Column(name = "amount", nullable = false)
    BigDecimal amount;

    @Column(name = "payment_status", nullable = false)
    String paymentStatus;

    @Column(name = "payment_date")
    LocalDateTime paymentDate;
}

