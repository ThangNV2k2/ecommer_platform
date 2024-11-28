package com.doan.backend.repositories;

import com.doan.backend.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
    Boolean existsByName(String name);

    Optional<Category> findByName(String name);

    Page<Category> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
