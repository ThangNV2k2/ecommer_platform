package com.doan.backend.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
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
public class PromotionRequest {
    @NotNull(message = "Name is required")
    String name;
    @NotNull(message = "Description is required")
    String description;
    @NotNull(message = "DiscountPercentage is required")
    BigDecimal discountPercentage;
    @NotNull(message = "Start-Date is not null")
    LocalDateTime startDate;
    @NotNull(message = "End-Date is not null")
    LocalDateTime endDate;
    Boolean isGlobal;
    Boolean isActive;
}
