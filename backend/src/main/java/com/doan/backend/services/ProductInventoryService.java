package com.doan.backend.services;

import com.doan.backend.dto.request.ProductInventoryRequest;
import com.doan.backend.dto.response.ApiResponse;
import com.doan.backend.dto.response.ProductInventoryResponse;
import com.doan.backend.entity.*;
import com.doan.backend.mapper.ProductInventoryMapper;
import com.doan.backend.repositories.ColorRepository;
import com.doan.backend.repositories.ProductInventoryRepository;
import com.doan.backend.repositories.ProductRepository;
import com.doan.backend.repositories.SizeRepository;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@AllArgsConstructor
public class ProductInventoryService {
    ProductInventoryRepository productInventoryRepository;
    ColorRepository colorRepository;
    SizeRepository sizeRepository;
    ProductRepository productRepository;
    ProductInventoryMapper productInventoryMapper;

    public ApiResponse<ProductInventoryResponse> createProductInventory(ProductInventoryRequest productInventoryRequest) {
        ProductInventory productInventory = productInventoryMapper.toProductInventory(productInventoryRequest);
        productInventory = productInventoryRepository.save(productInventory);
        return ApiResponse.<ProductInventoryResponse>builder()
                .code(200)
                .message("ProductInventory created successfully")
                .result(productInventoryMapper.toProductInventoryResponse(productInventory))
                .build();
    }

    public ApiResponse<ProductInventoryResponse> updateProduct(String id, ProductInventoryRequest productRequest) {
        ProductInventory productInventory = productInventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ProductInventory not found"));

        Product product = productRepository.findById(productRequest.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Color color = colorRepository.findById(productRequest.getColorId())
                .orElseThrow(() -> new RuntimeException("Color not found"));
        Size size = sizeRepository.findById(productRequest.getSizeId())
                .orElseThrow(() -> new RuntimeException("Size not found"));

        productInventory.setProduct(product);
        productInventory.setColor(color);
        productInventory.setSize(size);
        productInventory.setQuantity(productInventory.getQuantity());

        productInventory = productInventoryRepository.save(productInventory);
        return ApiResponse.<ProductInventoryResponse>builder()
                .code(200)
                .message("ProductInventory updated successfully")
                .result(productInventoryMapper.toProductInventoryResponse(productInventory))
                .build();
    }

    public ApiResponse<Void> deleteProduct(String id) {
        productInventoryRepository.deleteById(id);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("ProductInventory deleted successfully")
                .build();
    }

    public ApiResponse<ProductInventoryResponse> getProductInventoryById(String id) {
        ProductInventoryResponse productInventoryResponse = productInventoryRepository.findById(id)
                .map(productInventoryMapper::toProductInventoryResponse)
                .orElseThrow(() -> new RuntimeException("ProductInventory not found"));

        return ApiResponse.<ProductInventoryResponse>builder()
                .code(200)
                .message("ProductInventory retrieved successfully")
                .result(productInventoryResponse)
                .build();
    }


}
