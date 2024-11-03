package com.doan.backend.dto.request;

import com.doan.backend.enums.DiscountType;
import jakarta.validation.constraints.NotBlank;
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
public class DiscountRequest {

    @NotBlank(message = "Code is required")
    String code;

    @NotBlank(message = "Discount type is required")
    DiscountType discountType;

    @NotBlank(message = "Discount percentage is required")
    BigDecimal discountPercentage;

    BigDecimal discountValue;

    BigDecimal maxDiscountValue;

    @NotBlank(message = "Min Order Value is required")
    BigDecimal minOrderValue;

    @NotBlank(message = "Max uses is required")
    Integer maxUses;

    @NotBlank(message = "Expiry date is required")
    LocalDateTime expiryDate;

    LocalDateTime startDate;
}
