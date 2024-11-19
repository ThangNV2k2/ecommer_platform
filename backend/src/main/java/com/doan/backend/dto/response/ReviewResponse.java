package com.doan.backend.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewResponse {
    String id;
    String orderId;
    String productId;
    String userId;
    Double rating;
    String content;
}