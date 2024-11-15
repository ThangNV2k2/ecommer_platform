package com.doan.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;

@Validated
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
public class ReviewRequest {
    @NotBlank(message = "ProductId is required")
    String productId;

    @NotBlank(message = "UserId is required")
    String userId;
    @NotBlank(message = "OrderId is required")
    String orderId;
    Double rating;
    String content;
}
