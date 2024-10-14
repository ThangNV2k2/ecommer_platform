package com.doan.backend.entity;
import com.doan.backend.enums.LoyaltyTierEnum;
import com.doan.backend.enums.RoleEnum;
import com.doan.backend.enums.UserStatusEnum;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    UUID id;

    @Column(name = "email", unique = true, nullable = false)
    String email;

    @Column(name = "password")
    String password;

    @Column(name = "name")
    String name;

    @Column(name = "address")
    String address;

    @Column(name = "google_id")
    String googleId;

    @Enumerated(EnumType.STRING)
    @Column(name = "loyalty_tier")
    LoyaltyTierEnum loyaltyTier;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    UserStatusEnum status;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    Set<RoleEnum> roles = new HashSet<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    public boolean isCustomer() {
        return roles.contains(RoleEnum.CUSTOMER);
    }

    public boolean isAdmin() {
        return roles.contains(RoleEnum.ADMIN);
    }

    public boolean isShipper() {
        return roles.contains(RoleEnum.SHIPPER);
    }
}
