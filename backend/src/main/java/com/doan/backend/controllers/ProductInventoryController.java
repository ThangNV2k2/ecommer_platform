package com.doan.backend.controllers;

import com.doan.backend.dto.request.ProductInventoryRequest;
import com.doan.backend.dto.response.ApiResponse;
import com.doan.backend.services.ProductInventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product-inventory")
public class ProductInventory {
    @Autowired
    private ProductInventoryService productInventoryService;

    @PostMapping
    ApiResponse<?> createProductInventory(@RequestBody ProductInventoryRequest productInventoryRequest) {
        return productInventoryService.createProductInventory(productInventoryRequest);
    }

    @GetMapping
    ApiResponse<?> getProductInventoryByProductId(@RequestParam String productId) {
        return productInventoryService.getProductInventoryByProductId(productId);
    }

    @PutMapping
    ApiResponse<?> updateProductInventory(@RequestBody ProductInventoryRequest productInventoryRequest) {
        return productInventoryService.updateProductInventory(productInventoryRequest);
    }

    @DeleteMapping
    ApiResponse<?> deleteProductInventory(@RequestParam String id) {
        return productInventoryService.deleteProductInventory(id);
    }
}
