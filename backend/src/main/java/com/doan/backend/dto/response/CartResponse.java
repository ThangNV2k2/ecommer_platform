package com.doan.backend.dto.response;

import com.doan.backend.entity.CartItem;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
@AllArgsConstructor
public class CartResponse {
    String id;

    List<CartItem> cartItems;

    LocalDateTime createdAt;

    LocalDateTime updatedAt;
}
