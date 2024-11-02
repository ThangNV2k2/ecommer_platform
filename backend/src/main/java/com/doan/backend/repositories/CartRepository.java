package com.doan.backend.repositories;

import com.doan.backend.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, String> {
    @Query("SELECT c FROM Cart c LEFT JOIN FETCH c.cartItems WHERE c.id = :cartId")
    Optional<Cart> findByIdWithCartItems(@Param("cartId") String cartId);

    Optional<Cart> findByUserId(String userId);

}