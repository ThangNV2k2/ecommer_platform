package com.doan.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
public class ProductImageRequest {
    @NotBlank(message = "Product is required")
    String idProduct;
    @NotBlank(message = "ImageUrl is not null")
    List<String> imageUrl;
}
