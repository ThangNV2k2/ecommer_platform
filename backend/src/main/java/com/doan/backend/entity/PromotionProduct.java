package com.doan.backend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "promotion_products")
public class PromotionProduct {

    @Id
    @GeneratedValue
    UUID id;

    @ManyToOne
    @JoinColumn(name = "promotion_id")
    Promotion promotion;

    @ManyToOne
    @JoinColumn(name = "product_id")
    Product product;
}
