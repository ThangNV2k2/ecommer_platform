package com.doan.backend.services;

import com.doan.backend.dto.request.ProductRequest;
import com.doan.backend.dto.response.ApiResponse;
import com.doan.backend.dto.response.ProductResponse;
import com.doan.backend.entity.Category;
import com.doan.backend.entity.Product;
import com.doan.backend.entity.Promotion;
import com.doan.backend.entity.PromotionProduct;
import com.doan.backend.mapper.ProductMapper;
import com.doan.backend.repositories.CategoryRepository;
import com.doan.backend.repositories.ProductRepository;
import com.doan.backend.repositories.PromotionProductRepository;
import com.doan.backend.repositories.PromotionRepository;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@AllArgsConstructor
public class ProductService {
    ProductRepository productRepository;
    CategoryRepository categoryRepository;
    ProductMapper productMapper;
    PromotionService promotionService;
    PromotionRepository promotionRepository;
    PromotionProductRepository promotionProductRepository;


    private void savePromotionProducts(Product product, List<String> promotionIds) {
        Optional<Promotion> existingPromotion = promotionProductRepository.findActivePromotionByProductId(product.getId(), LocalDateTime.now());
        if (existingPromotion.isPresent()) {
            throw new RuntimeException("An active promotion already exists for this product.");
        }

        if (promotionIds != null && !promotionIds.isEmpty()) {
            List<PromotionProduct> promotionProducts = new ArrayList<>();

            for (String promotionId : promotionIds) {
                Promotion promotion = promotionRepository.findById(promotionId)
                        .orElseThrow(() -> new RuntimeException("Promotion not found"));

                PromotionProduct promotionProduct = new PromotionProduct();
                promotionProduct.setPromotion(promotion);
                promotionProduct.setProduct(product);
                promotionProducts.add(promotionProduct);
            }

            promotionProductRepository.saveAll(promotionProducts);
        }
    }

    public ApiResponse<Void> createProduct(ProductRequest productRequest) {
        Product product = productMapper.toProduct(productRequest);
        savePromotionProducts(product, productRequest.getPromotionIds());
        productRepository.save(product);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Product created successfully")
                .build();
    }

    public ApiResponse<Void> updateProduct(String id, ProductRequest productRequest) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Category category = categoryRepository.findById(productRequest.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        List<PromotionProduct> existingPromotionProducts = promotionProductRepository.findPromotionProductsByProductId(product.getId());
        promotionProductRepository.deleteAll(existingPromotionProducts);
        savePromotionProducts(product, productRequest.getPromotionIds());


        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setIsActive(productRequest.getIsActive());
        product.setMainImage(productRequest.getMainImage());
        product.setCategory(category);

        productRepository.save(product);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Product updated successfully")
                .build();
    }

    public ApiResponse<Void> deleteProduct(String id) {
        productRepository.deleteById(id);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Product deleted successfully")
                .build();
    }

    public ApiResponse<ProductResponse> getProductById(String id) {
        ProductResponse productResponse = productRepository.findById(id)
                .map(productMapper::toProductResponse)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Optional<Promotion> promotionOptional = promotionProductRepository.findActivePromotionByProductId(id, LocalDateTime.now());

        productResponse.setDiscountPercentage(
                promotionOptional.map(Promotion::getDiscountPercentage).orElse(BigDecimal.ZERO)
        );

        return ApiResponse.<ProductResponse>builder()
                .code(200)
                .message("Product retrieved successfully")
                .result(productResponse)
                .build();
    }

    @Cacheable(value = "searchProductsCache", key = "#name + '-' + #categoryId + '-' + #pageable")
    public ApiResponse<Page<ProductResponse>> searchProducts(String name, String categoryId, Pageable pageable) {
        Page<Product> products;
        if (name != null && categoryId != null) {
            products = productRepository.findByNameContainingIgnoreCaseAndCategory_Id(name, categoryId, pageable);
        } else if (name != null) {
            products = productRepository.findByNameContainingIgnoreCase(name, pageable);
        } else if (categoryId != null) {
            products = productRepository.findByCategory_Id(categoryId, pageable);
        } else {
            products = productRepository.findAll(pageable);
        }

        Page<ProductResponse> productResponsePage = products.map(product -> {
            ProductResponse response = productMapper.toProductResponse(product);
            BigDecimal discountPercentage = promotionProductRepository.findActivePromotionByProductId(product.getId(), LocalDateTime.now()).map(Promotion::getDiscountPercentage).orElse(BigDecimal.ZERO);
            response.setDiscountPercentage(discountPercentage);
            return response;
        });
        return ApiResponse.<Page<ProductResponse>>builder()
                .code(200)
                .message("Products retrieved successfully")
                .result(productResponsePage)
                .build();
    }
}
