package com.doan.backend.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Validated
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
@AllArgsConstructor
public class PromotionResponse {
    String id;
    String name;
    String description;
    BigDecimal discountPercentage;
    LocalDateTime startDate;
    LocalDateTime endDate;
    Boolean isGlobal;
    Boolean isActive;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
