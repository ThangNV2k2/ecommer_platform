package com.doan.backend.controllers;

import com.doan.backend.dto.request.CartItemRequest;
import com.doan.backend.dto.response.ApiResponse;
import com.doan.backend.dto.response.CartResponse;
import com.doan.backend.entity.CartItem;
import com.doan.backend.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @GetMapping("/{id}")
    public ApiResponse<CartResponse> getCart(@PathVariable String id) {
        return cartService.getCartByUserId(id);
    }

    @PostMapping("/{id}")
    public ApiResponse<CartItem> createOrUpdateCartItem(@PathVariable String id, @RequestBody CartItemRequest cartItemRequest) {
        return cartService.addOrUpdateCartItem(id, cartItemRequest);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteCartItem(@PathVariable String id) {
        return cartService.deleteCartItem(id);
    }
}
