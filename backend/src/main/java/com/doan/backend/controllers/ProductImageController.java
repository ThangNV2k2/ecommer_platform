package com.doan.backend.controllers;

import com.doan.backend.dto.response.ApiResponse;
import com.doan.backend.dto.response.ProductImageResponse;
import com.doan.backend.services.ProductImageService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/product-image")
public class ProductImageController {
    ProductImageService productImageService;

    @PostMapping
    public ApiResponse<ProductImageResponse> uploadImage(@PathVariable String productId,
                                                         @RequestParam("files") List<MultipartFile> files){
        return productImageService.updateProductImage(productId,files);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteImage(@PathVariable String id){
        return productImageService.deleteProductImage(id);
    }
}
