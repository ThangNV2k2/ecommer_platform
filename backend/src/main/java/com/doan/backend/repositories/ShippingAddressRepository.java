package com.doan.backend.repositories;

import com.doan.backend.entity.ShippingAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShippingAddressRepository extends JpaRepository<ShippingAddress, String> {
    Iterable<ShippingAddress> findByUserId(String userId);
    Boolean existsByUserIdAndIsDefault(String userId, Boolean isDefault);
    ShippingAddress findByUserIdAndIsDefault(String userId, Boolean isDefault);
}
