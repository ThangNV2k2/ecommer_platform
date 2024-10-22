package com.doan.backend.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryResponse {
    String id;

    String name;

    String description;

    Boolean isActive;

    LocalDateTime createdAt;

    LocalDateTime updatedAt;
}
