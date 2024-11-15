package com.doan.backend.services;

import com.doan.backend.dto.response.ApiResponse;
import com.doan.backend.entity.Product;
import com.doan.backend.entity.ProductImage;
import com.doan.backend.repositories.ProductImageRepository;
import com.doan.backend.repositories.ProductRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class ProductImageService {
    ProductImageRepository productImageRepository;
    ProductRepository productRepository;
    ImageService imageService;

    public ApiResponse<ProductImage> updateProductImage(String id, List<MultipartFile> files){
        Optional<Product> productOptional = productRepository.findById(id);
        if(productOptional.isEmpty()){
            throw new RuntimeException("Product not found");
        }

        for(MultipartFile file : files){
            String newUrl = imageService.uploadImage(file).getResult();
            ProductImage productImage = new ProductImage();
            productImage.setImageUrl(newUrl);
            productImage.setProduct(productOptional.get());
            productImageRepository.save(productImage);
        }

        return ApiResponse.<ProductImage>builder()
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
