package com.doan.backend.controllers;

import com.doan.backend.dto.request.ProductRequest;
import com.doan.backend.dto.response.ApiResponse;
import com.doan.backend.dto.response.ProductResponse;
import com.doan.backend.services.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PUBLIC, makeFinal = true)
public class ProductController {

    ProductService productService;

    @GetMapping("/{id}")
    ApiResponse<ProductResponse> getProductById(@PathVariable String id) {
        return productService.getProductById(id);

    }

    @GetMapping
    ApiResponse<Page<ProductResponse>> searchProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String categoryId,
            Pageable pageable) {
        return productService.searchProducts(name, categoryId, pageable);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    ApiResponse<ProductResponse> createProduct(@RequestBody ProductRequest productRequest) {
        return productService.createProduct(productRequest);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    ApiResponse<ProductResponse> updateProduct(
            @PathVariable String id,
            @RequestBody ProductRequest productRequest) {
        return productService.updateProduct(id, productRequest);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    ApiResponse<Void> deleteProduct(@PathVariable String id) {
        return productService.deleteProduct(id);
    }

}