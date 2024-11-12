package com.doan.backend.controllers;

import com.doan.backend.dto.response.ApiResponse;
import com.doan.backend.dto.response.ProductImageResponse;
import com.doan.backend.entity.ProductImage;
import com.doan.backend.services.ProductImageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/product-image")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PUBLIC, makeFinal = true)
public class ProductImageController {
    ProductImageService productImageService;

    @PostMapping("/{productId}")
    public ApiResponse<ProductImage> uploadImage(@PathVariable String productId,
                                                 @RequestParam("files") List<MultipartFile> files){
        return productImageService.updateProductImage(productId,files);
    }

    @PostMapping()
    public ApiResponse<String> deleteImage(@RequestBody List<String> ids){
        return productImageService.deleteProductImage(ids);
    }
}
