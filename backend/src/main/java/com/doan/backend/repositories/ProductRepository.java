package com.doan.backend.repositories;

import com.doan.backend.dto.response.ProductResponse;
import com.doan.backend.dto.response.ProductSearch;
import com.doan.backend.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Product> findByCategory_Id(String categoryId, Pageable pageable);
    Page<Product> findByNameContainingIgnoreCaseAndCategory_Id(String name, String categoryId, Pageable pageable);

    @Query(nativeQuery = true, value = "CALL SearchProducts(:searchName, :categoryId)")
    List<Product> searchProducts(@Param("searchName") String searchName,
                                       @Param("categoryId") String categoryId);
}
