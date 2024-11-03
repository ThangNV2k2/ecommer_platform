package com.doan.backend.repositories;

import com.doan.backend.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, String> {
    Boolean existsByCode(String code);
    Optional<Discount> findByCode(String code);
    Iterable<Discount> findByAutoApplyTrueAndExpiryDateAfter(LocalDateTime now);
    Optional<Discount> findByCodeAndExpiryDateAfter(String code, LocalDateTime now);
}
