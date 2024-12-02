package com.doan.backend.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@FieldDefaults(level = AccessLevel.PUBLIC)
@Data
@Builder
@AllArgsConstructor
public class ProductRevenueResponse {
    String product_name;
    Integer total_quantity;
    BigDecimal total_revenue;
}
