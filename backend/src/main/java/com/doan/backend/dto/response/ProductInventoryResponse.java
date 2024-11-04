package com.doan.backend.dto.response;


import com.doan.backend.entity.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductInventoryResponse {
    String id;

    String idProduct;

    Size size;

    Integer quantity;
}
