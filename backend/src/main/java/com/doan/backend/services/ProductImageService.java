package com.doan.backend.services;

import com.doan.backend.dto.request.ProductImageRequest;
import com.doan.backend.dto.response.ApiResponse;
import com.doan.backend.dto.response.ProductImageResponse;
import com.doan.backend.entity.Product;
import com.doan.backend.entity.ProductImage;
import com.doan.backend.mapper.ProductImageMapper;
import com.doan.backend.repositories.ProductImageRepository;
import com.doan.backend.repositories.ProductRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class ProductImageService {
    ProductImageRepository productImageRepository;
    ProductRepository productRepository;

    public ApiResponse<ProductImageResponse> createProductImage(ProductImageRequest productImageRequest){
        Product product = productRepository.findById(productImageRequest.getIdProduct())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        for(String url : productImageRequest.getImageUrl()){
            ProductImage productImage = new ProductImage();
            productImage.setImageUrl(url);
            productImage.setProduct(product);
            productImageRepository.save(productImage);
        }

        return ApiResponse.<ProductImageResponse>builder()
                .message("Update product image successfully")
                .code(200)
                .build();
    }

    // Delete
    public ApiResponse<String> deleteProductImage(List<String> ids) {
        productImageRepository.deleteAllById(ids);
        return ApiResponse.<String>builder()
                .message("Delete product image successfully")
                .code(200)
                .build();
    }
}
