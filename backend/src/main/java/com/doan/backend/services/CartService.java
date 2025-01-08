package com.doan.backend.services;

import com.doan.backend.dto.request.CartItemRequest;
import com.doan.backend.dto.response.ApiResponse;
import com.doan.backend.dto.response.CartItemResponse;
import com.doan.backend.dto.response.CartResponse;
import com.doan.backend.dto.response.ProductResponse;
import com.doan.backend.entity.Cart;
import com.doan.backend.entity.CartItem;
import com.doan.backend.entity.ProductInventory;
import com.doan.backend.entity.Promotion;
import com.doan.backend.mapper.CartItemMapper;
import com.doan.backend.mapper.CartMapper;
import com.doan.backend.mapper.PromotionMapper;
import com.doan.backend.repositories.*;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class CartService {
    CartRepository cartRepository;
    CartItemRepository cartItemRepository;
    ProductInventoryRepository productInventoryRepository;
    UserRepository userRepository;
    CartMapper cartMapper;
    CartItemMapper cartItemMapper;
    PromotionProductRepository promotionProductRepository;
    PromotionMapper promotionMapper;

    public ApiResponse<CartResponse> getCartByUserId(String userId) {
        Optional<Cart> cartOptional = cartRepository.findByUserId(userId);

        if (cartOptional.isPresent()) {
            Cart cart = cartOptional.get();
            CartResponse cartResponse = cartMapper.toCartResponse(cart);

            List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());
            List<CartItemResponse> cartItemResponses = cartItemMapper.tocartItemResponseList(cartItems);

            cartItemResponses.forEach(cartItemResponse -> {
                ProductResponse productResponse = cartItemResponse.getProduct();
                List<Promotion> promotionApply = promotionProductRepository.findPromotionApplyByProductId(
                        productResponse.getId(),
                        LocalDateTime.now()
                );

                Optional<Promotion> promotionOptional = promotionApply.stream().findFirst();
                productResponse.setDiscountPercentage(
                        promotionOptional.map(Promotion::getDiscountPercentage).orElse(BigDecimal.ZERO)
                );
                productResponse.setPromotionResponse(
                        promotionOptional.map(promotionMapper::toPromotionResponse).orElse(null)
                );
            });

            cartResponse.setCartItems(cartItemResponses);

            return ApiResponse.<CartResponse>builder()
                    .code(200)
                    .message("Get cart successfully")
                    .result(cartResponse)
                    .build();
        } else {
            CartResponse newCartResponse = createCart(userId);
            return ApiResponse.<CartResponse>builder()
                    .code(200)
                    .message("Get cart successfully")
                    .result(newCartResponse)
                    .build();
        }
    }

    private ProductInventory validateProductInventory(CartItemRequest cartItemRequest) {
        ProductInventory productInventory = productInventoryRepository.findByProductIdAndSizeId(
                        cartItemRequest.getProductId(), cartItemRequest.getSizeId())
                .orElseThrow(() -> new RuntimeException("Product inventory not found"));

        if (productInventory.getQuantity() < cartItemRequest.getQuantity()) {
            throw new RuntimeException("Insufficient stock for product: " + productInventory.getProduct().getName());
        }
        return productInventory;
    }

    public ApiResponse<CartItemResponse> addCartItem(CartItemRequest cartItemRequest) {
        ProductInventory productInventory = validateProductInventory(cartItemRequest);
        Cart cart = cartRepository.findById(cartItemRequest.getCartId()).orElseThrow(() -> new RuntimeException("Cart not found"));
        CartItem newCartItem = new CartItem();

        newCartItem.setCart(cart);
        newCartItem.setProduct(productInventory.getProduct());
        newCartItem.setSize(productInventory.getSize());
        newCartItem.setQuantity(cartItemRequest.getQuantity());

        CartItemResponse cartItemResponse = cartItemMapper.toCartItemResponse(cartItemRepository.save(newCartItem));

        return ApiResponse.<CartItemResponse>builder()
                .code(200)
                .message("Cart item added successfully")
                .result(cartItemResponse)
                .build();
    }

    public ApiResponse<CartItemResponse> updateCartItem(String cartItemId, CartItemRequest cartItemRequest) {
        ProductInventory productInventory = validateProductInventory(cartItemRequest);

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        cartItem.setQuantity(cartItemRequest.getQuantity());
        cartItem.setProduct(productInventory.getProduct());
        cartItem.setSize(productInventory.getSize());
        CartItemResponse cartItemResponse = cartItemMapper.toCartItemResponse(cartItemRepository.save(cartItem));
        return ApiResponse.<CartItemResponse>builder()
                .code(200)
                .message("Cart item updated successfully")
                .result(cartItemResponse)
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

        CartResponse cartResponse = cartMapper.toCartResponse(cartRepository.save(cart));
        cartResponse.setCartItems(Collections.emptyList());
        return cartResponse;
    }
}
