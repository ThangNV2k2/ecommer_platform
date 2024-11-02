package com.doan.backend.services;

import com.doan.backend.dto.request.CartItemRequest;
import com.doan.backend.dto.response.ApiResponse;
import com.doan.backend.dto.response.CartResponse;
import com.doan.backend.entity.Cart;
import com.doan.backend.entity.CartItem;
import com.doan.backend.entity.ProductInventory;
import com.doan.backend.mapper.CartMapper;
import com.doan.backend.repositories.*;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class CartService {
    CartRepository cartRepository;
    CartItemRepository cartItemRepository;
    ProductInventoryRepository productInventoryRepository;
    ProductRepository productRepository;
    UserRepository userRepository;
    CartMapper cartMapper;

    public ApiResponse<CartResponse> getCartByUserId(String userId) {
        Optional<Cart> cartOptional = cartRepository.findByUserId(userId);
        if(cartOptional.isPresent()) {
            return ApiResponse.<CartResponse>builder()
                    .code(200)
                    .message("get cart successfully")
                    .result(cartMapper.toCartResponse(cartOptional.get()))
                    .build();
        }

        else {
            return ApiResponse.<CartResponse>builder()
                    .code(200)
                    .message("get cart successfully")
                    .result(createCart(userId))
                    .build();
        }
    }

    @Transactional
    public ApiResponse<CartItem> addOrUpdateCartItem(String cartItemId, CartItemRequest cartItemRequest) {
        ProductInventory productInventory = productInventoryRepository.findByProductIdAndSizeId(
                        cartItemRequest.getProductId(), cartItemRequest.getSizeId())
                .orElseThrow(() -> new RuntimeException("Product inventory not found"));

        if (productInventory.getQuantity() < cartItemRequest.getQuantity()) {
            throw new RuntimeException("Insufficient stock for product: " + productInventory.getProduct().getName());
        }

        Optional<CartItem> existingCartItem = cartItemRepository.findById(cartItemId);
        if (existingCartItem.isPresent()) {
            CartItem cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + cartItemRequest.getQuantity());
        } else {
            CartItem newCartItem = new CartItem();
            newCartItem.setProduct(productInventory.getProduct());
            newCartItem.setCart(cartRepository.findById(cartItemRequest.getCartId()).orElseThrow(() -> new RuntimeException("Cart not found")));
            newCartItem.setQuantity(cartItemRequest.getQuantity());
        }
         return ApiResponse.<CartItem>builder()
                .code(200)
                .message("add or update cart item successfully")
                .result(cartItemRepository.save(new CartItem()))
                .build();
    }

    public ApiResponse<Void> deleteCartItem(String cartItemId) {
        cartItemRepository.deleteById(cartItemId);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("delete cart item successfully")
                .build();
    }

    private CartResponse createCart(String userId) {
        Cart cart = new Cart();
        cart.setUser(userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found")));
        cart.setCartItems(new ArrayList<>());

        return cartMapper.toCartResponse(cartRepository.save(cart));
    }
}
