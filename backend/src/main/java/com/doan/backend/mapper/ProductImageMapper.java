package com.doan.backend.mapper;


import com.doan.backend.dto.request.ProductImageRequest;
import com.doan.backend.dto.response.ProductImageResponse;
import com.doan.backend.entity.ProductImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductImageMapper {
//    @Mapping(source = "idProduct", target = "product.id")
//    ProductImage toProductImage(ProductImageRequest productImageRequest);
//
//    @Mapping(source = "product.id", target = "idProduct")
//    ProductImageResponse toProductImageResponse(ProductImage productImage);
}
