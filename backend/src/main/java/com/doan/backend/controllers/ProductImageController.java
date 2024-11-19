package com.doan.backend.controllers;

import com.doan.backend.dto.request.DeleteProductImageRequest;
import com.doan.backend.dto.request.ProductImageRequest;
import com.doan.backend.dto.response.ApiResponse;
import com.doan.backend.dto.response.ProductImageResponse;
import com.doan.backend.services.ProductImageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product-image")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PUBLIC, makeFinal = true)
@PreAuthorize("hasRole('ADMIN')")
public class ProductImageController {
    ProductImageService productImageService;

    @PostMapping()
    public ApiResponse<ProductImageResponse> createImage(@RequestBody ProductImageRequest productImageRequest) {
        return productImageService.createProductImage(productImageRequest);
    }

    @DeleteMapping("/delete")
    public ApiResponse<String> deleteImage(@RequestBody DeleteProductImageRequest deleteProductImageRequest) {
        return productImageService.deleteProductImage(deleteProductImageRequest);
    }
}
