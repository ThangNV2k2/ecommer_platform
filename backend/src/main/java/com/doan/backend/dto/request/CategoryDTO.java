package com.doan.backend.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryDTO {
    //    String id;
    String name;
    String description;
    Boolean isActive;
    //LocalDateTime createdAt;
    //LocalDateTime updatedAt=null;
}
