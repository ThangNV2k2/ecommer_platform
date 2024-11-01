package com.doan.backend.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;

@Validated
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
public class ProductInventoryRequest {
    @NotNull(message = "ProductId is required")
    String productId;
    @NotNull(message = "ColorId is required")
    String colorId;
    @NotNull(message = "SizeId is required")
    String sizeId;
    @NotNull(message = "Quantity is required")
    Integer quantity;
}
