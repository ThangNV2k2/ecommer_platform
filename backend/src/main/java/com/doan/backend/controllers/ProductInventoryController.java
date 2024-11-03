package com.doan.backend.controllers;

import com.doan.backend.dto.request.ProductInventoryRequest;
import com.doan.backend.dto.response.ApiResponse;
import com.doan.backend.services.ProductInventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product-inventory")
public class ProductInventoryController {
    @Autowired
    private ProductInventoryService productInventoryService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<?> createProductInventory(@RequestBody @Validated ProductInventoryRequest productInventoryRequest) {
        return productInventoryService.createProductInventory(productInventoryRequest);
    }

    @GetMapping
    ApiResponse<?> getProductInventoryByProductId(@RequestParam String productId) {
        return productInventoryService.getProductInventoryByProductId(productId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    ApiResponse<?> updateProductInventory(@PathVariable String id, @RequestBody @Validated ProductInventoryRequest productInventoryRequest) {
        return productInventoryService.updateProductInventory(id, productInventoryRequest);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    ApiResponse<?> deleteProductInventory(@PathVariable String id) {
        return productInventoryService.deleteProductInventory(id);
    }
}
