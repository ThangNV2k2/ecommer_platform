package com.doan.backend.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductStatisticResponse {
    String productName;
    LocalDateTime date;
    String size;
    BigDecimal price;
    Integer quantity;
    BigDecimal discountPercentage;
}
