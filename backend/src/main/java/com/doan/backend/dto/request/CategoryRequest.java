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
public class CategoryRequest {

    @NotNull(message = "Name is required")
    String name;
    String description;
    @NotNull(message = "isActive is required")
    Boolean isActive;
}
