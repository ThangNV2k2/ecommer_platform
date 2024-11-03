package com.doan.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.List;

@Validated
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
public class ProductRequest {
    @NotBlank(message = "Name is required")
    String name;
    String description;
    @NotBlank(message = "Price is required")
    BigDecimal price;
    @NotBlank(message = "CategoryId is required")
    String categoryId;

    @NotBlank(message = "IsActive is required")
    Boolean isActive;

    List<String> promotionIds;
    String mainImage;
}
