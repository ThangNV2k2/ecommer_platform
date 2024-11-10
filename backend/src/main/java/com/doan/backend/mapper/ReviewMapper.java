package com.doan.backend.mapper;

import com.doan.backend.dto.request.ReviewRequest;
import com.doan.backend.dto.response.ReviewResponse;
import com.doan.backend.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    @Mapping(source = "productId", target = "product.id")
    @Mapping(source = "customerId", target = "customer.id")
    @Mapping(source = "orderId", target = "order.id")
    Review toReview(ReviewRequest reviewRequest);

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "order.id", target = "orderId")
    ReviewResponse toReviewResponse(Review review);
}
