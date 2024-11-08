package com.doan.backend.repositories;

import com.doan.backend.entity.Promotion;
import com.doan.backend.entity.PromotionProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PromotionProductRepository extends JpaRepository<PromotionProduct, String> {
    @Query("SELECT pp.promotion FROM PromotionProduct pp " +
            "WHERE pp.product.id = :productId " +
            "AND pp.promotion.isActive = true " +
            "AND pp.promotion.startDate <= :currentDate " +
            "AND pp.promotion.endDate >= :currentDate")
    Optional<Promotion> findActivePromotionByProductId(
            @Param("productId") String productId,
            @Param("currentDate") LocalDateTime currentDate);
    List<PromotionProduct> findPromotionProductsByProductId(String productId);
}