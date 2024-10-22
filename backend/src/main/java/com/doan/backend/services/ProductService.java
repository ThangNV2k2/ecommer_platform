package com.doan.backend.services;

import com.doan.backend.dto.request.ProductRequest;
import com.doan.backend.dto.response.ApiResponse;
import com.doan.backend.dto.response.ProductResponse;
import com.doan.backend.entity.Category;
import com.doan.backend.entity.Product;
import com.doan.backend.mapper.ProductMapper;
import com.doan.backend.repositories.CategoryRepository;
import com.doan.backend.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@AllArgsConstructor
public class ProductService {

    ProductRepository productRepository;

    CategoryRepository categoryRepository;

    ProductMapper productMapper;

    public ApiResponse<ProductResponse> createProduct(ProductRequest productRequest) {
        Product product = productMapper.toProduct(productRequest);
        product = productRepository.save(product);
        return ApiResponse.<ProductResponse>builder()
                .code(200)
                .message("Product created successfully")
                .result(productMapper.toProductResponse(product))
                .build();
    }

    public ApiResponse<ProductResponse> updateProduct(String id, ProductRequest productRequest) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Category category = categoryRepository.findById(productRequest.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setIsActive(productRequest.getIsActive());
        product.setMainImage(productRequest.getMainImage());
        product.setCategory(category);

        product = productRepository.save(product);
        return ApiResponse.<ProductResponse>builder()
                .code(200)
                .message("Product updated successfully")
                .result(productMapper.toProductResponse(product))
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

        return ApiResponse.<ProductResponse>builder()
                .code(200)
                .message("Product retrieved successfully")
                .result(productResponse)
                .build();
    }

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

        Page<ProductResponse> productResponsePage = products.map(productMapper::toProductResponse);
        return ApiResponse.<Page<ProductResponse>>builder()
                .code(200)
                .message("Products retrieved successfully")
                .result(productResponsePage)
                .build();
    }
}
