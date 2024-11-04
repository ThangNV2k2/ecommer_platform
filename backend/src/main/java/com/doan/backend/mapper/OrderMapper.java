package com.doan.backend.mapper;

import com.doan.backend.dto.response.OrderResponse;
import com.doan.backend.entity.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class})
public interface OrderMapper {
    OrderResponse toOrderResponse(Order order);
    Iterable<OrderResponse> toOrderResponseIterable(Iterable<Order> orders);
}
