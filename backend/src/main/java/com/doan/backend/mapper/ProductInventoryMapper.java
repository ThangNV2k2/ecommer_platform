package com.doan.backend.mapper;

import com.doan.backend.dto.request.ProductInventoryRequest;
import com.doan.backend.dto.response.ProductInventoryResponse;
import com.doan.backend.entity.ProductInventory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class})
public interface ProductInventoryMapper {
    @Mapping(source = "productInventoryRequest.productId", target = "product.id")
    @Mapping(source = "productInventoryRequest.colorId", target = "color.id")
    @Mapping(source = "productInventoryRequest.sizeId", target = "size.id")
    ProductInventory toProductInventory(ProductInventoryRequest productInventoryRequest);

    @Mapping(source = "product", target = "productResponse")
    @Mapping(source = "color", target = "colorResponse")
    @Mapping(source = "size", target = "sizeResponse")
    ProductInventoryResponse toProductInventoryResponse(ProductInventory productInventory);
}
