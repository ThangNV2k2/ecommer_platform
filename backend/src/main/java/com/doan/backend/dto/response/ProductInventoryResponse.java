package com.doan.backend.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;

@Validated
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
@AllArgsConstructor
public class ProductInventoryResponse {
    String id;
    ProductResponse productResponse;
    ColorResponse colorResponse;
    SizeResponse sizeResponse;
    Integer quantity;
}
