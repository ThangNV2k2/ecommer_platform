package com.doan.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountRepository extends JpaRepository<DiscountRepository, String> {

}
