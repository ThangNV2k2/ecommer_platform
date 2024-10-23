package com.doan.backend.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;

@Validated
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
public class ProductRequest {
    @NotNull(message = "Name is required")
    String name;
    String description;
    @NotNull(message = "Price is required")
    BigDecimal price;
    @NotNull(message = "CategoryId is required")
    String categoryId;

    @NotNull(message = "IsActive is required")
    Boolean isActive;

    String mainImage;
}
