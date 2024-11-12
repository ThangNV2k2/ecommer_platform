package com.doan.backend.repositories;

import com.doan.backend.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, String> {
    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Product> findByCategory_Id(String categoryId, Pageable pageable);

    @Query("SELECT p FROM Product p " +
           "INNER JOIN OrderItem oi ON p.Id = oi.productId" +
           "WHERE (:name IS NULL OR LOWER(p.Name) LIKE LOWER(CONCAT('%',:name,'%')))" +
           "AND (:categoryId IS NULL OR p.categoryId = :categoryId)" +
           "GROUP BY p.id" +
           "ORDER BY p.rating DESC, SUM(oi.quantity) DESC")
    Page<Product> findByNameContainingIgnoreCaseAndCategory_Id(String name, String categoryId, Pageable pageable);
}
