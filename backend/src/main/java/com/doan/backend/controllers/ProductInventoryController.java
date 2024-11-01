package com.doan.backend.controllers;

import com.doan.backend.dto.request.ProductInventoryRequest;
import com.doan.backend.dto.response.ApiResponse;
import com.doan.backend.dto.response.ProductInventoryResponse;
import com.doan.backend.services.ProductInventoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product-inventory")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PUBLIC, makeFinal = true)
public class ProductInventoryController {
    ProductInventoryService productInventoryService;

    @GetMapping("/{id}")
    ApiResponse<ProductInventoryResponse> getProductById(@PathVariable String id) {
        return productInventoryService.getProductInventoryById(id);

    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    ApiResponse<ProductInventoryResponse> createProduct(@RequestBody ProductInventoryRequest productInventoryRequest) {
        return productInventoryService.createProductInventory(productInventoryRequest);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    ApiResponse<ProductInventoryResponse> updateProductInventory(
            @PathVariable String id,
            @RequestBody ProductInventoryRequest productInventoryRequest) {
        return productInventoryService.updateProduct(id, productInventoryRequest);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    ApiResponse<Void> deleteProduct(@PathVariable String id) {
        return productInventoryService.deleteProduct(id);
    }
}
