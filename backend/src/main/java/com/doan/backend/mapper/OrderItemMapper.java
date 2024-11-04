package com.doan.backend.mapper;

import com.doan.backend.dto.response.OrderItemResponse;
import com.doan.backend.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    @Mapping(source = "product", target = "productResponse")
    OrderItemResponse toOrderItemResponse(OrderItem orderItem);
}
