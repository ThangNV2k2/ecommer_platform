package com.doan.backend.dto.response;

import com.doan.backend.enums.LoyaltyTierEnum;
import com.doan.backend.enums.RoleEnum;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String id;
    String email;
    String name;
    Boolean isActive;
    LoyaltyTierEnum loyaltyTier;
    Set<RoleEnum> roles;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
