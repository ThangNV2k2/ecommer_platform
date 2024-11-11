package com.doan.backend.services;

import com.doan.backend.dto.response.ApiResponse;
import com.doan.backend.dto.response.ProductImageResponse;
import com.doan.backend.entity.Product;
import com.doan.backend.entity.ProductImage;
import com.doan.backend.mapper.ProductImageMapper;
import com.doan.backend.repositories.ProductImageRepository;
import com.doan.backend.repositories.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class ProductImageService {
    ProductImageRepository productImageRepository;
    ProductRepository productRepository;
    ImageService imageService;
    ProductImageMapper productImageMapper;

    public ApiResponse<ProductImageResponse> updateProductImage(String id, List<MultipartFile> files){
        Optional<Product> productOptional = productRepository.findById(id);
        if(productOptional.isEmpty()){
            throw new RuntimeException("Product not found");
        }

        for(MultipartFile file : files){
            String newUrl = imageService.uploadImage(file).getResult();
            ProductImage productImage = new ProductImage();
            productImage.setProduct(productOptional.get());
            productImage.setImageUrl(newUrl);

            ProductImageResponse productImageResponse = productImageMapper.toProductImageResponse(productImageRepository.save(productImage));
        }

        return ApiResponse.<ProductImageResponse>builder()
                .message("Update product inventory successfully")
                .code(200)
                .build();
    }

    // Delete
    public ApiResponse<String> deleteProductImage(String id) {
        productImageRepository.deleteById(id);
        return ApiResponse.<String>builder()
                .message("Delete product image successfully")
                .result(id)
                .build();
    }
}
