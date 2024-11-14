package com.doan.backend.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
@AllArgsConstructor
public class ProductSearch {
    String id;
    String name;
    BigDecimal price;
    String description;
    Double rating;
    String main_image;
    String category_name;
    BigDecimal total_sold;
}
