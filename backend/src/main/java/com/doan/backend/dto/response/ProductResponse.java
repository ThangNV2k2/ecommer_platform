package com.doan.backend.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
@AllArgsConstructor
public class ProductResponse {
    String id;
    String name;
    String description;
    BigDecimal price;
    CategoryResponse categoryResponse;
    Double rating;
    Boolean isActive;
    BigDecimal discountPercentage;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    String mainImage;
}
