package com.doan.backend.repositories;

import com.doan.backend.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion,String> {
    Boolean existsByName(String name);
}
