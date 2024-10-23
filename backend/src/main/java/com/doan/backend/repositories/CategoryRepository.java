package com.doan.backend.repositories;

import com.doan.backend.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
    Boolean existsByName(String name);
}
