package com.doan.backend.services;

import com.doan.backend.dto.request.ReviewRequest;
import com.doan.backend.dto.response.ApiResponse;
import com.doan.backend.dto.response.ReviewResponse;
import com.doan.backend.entity.Order;
import com.doan.backend.entity.Product;
import com.doan.backend.entity.Review;
import com.doan.backend.mapper.ReviewMapper;
import com.doan.backend.repositories.OrderRepository;
import com.doan.backend.repositories.ProductRepository;
import com.doan.backend.repositories.ReviewRepository;
import com.doan.backend.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@AllArgsConstructor
public class ReviewService {
    ReviewRepository reviewRepository;
    ProductRepository productRepository;
    UserRepository userRepository;
    OrderRepository orderRepository;
    ReviewMapper reviewMapper;

    public ApiResponse<ReviewResponse> createReview(ReviewRequest reviewRequest) {
        Optional<Product> product = productRepository.findById(reviewRequest.getProductId());
        Optional<Order> order = orderRepository.findById(reviewRequest.getOrderId());

        // Kiểm tra nếu sản phẩm thuộc đơn hàng đã thanh toán của khách hàng
        boolean hasPaidOrderWithProduct = orderRepository.existOrderCompletedByUserId(
                reviewRequest.getUserId(), reviewRequest.getProductId());

        if (!hasPaidOrderWithProduct) {
            throw new RuntimeException("Product not in any paid order of this customer");
        }

        Review review = reviewMapper.toReview(reviewRequest);
        review.setProduct(product.get());
        review.setUser(order.get().getUser());
        review.setOrder(order.get());
        review.setRating(reviewRequest.getRating());
        review.setContent(reviewRequest.getContent());

        Review savedReview = reviewRepository.save(review);
        // Tính và cập nhật rating trung bình cho sản phẩm
        calculateAndUpdateProductRating(product.get());

        return ApiResponse.<ReviewResponse>builder()
                .message("Create review successfully")
                .result(reviewMapper.toReviewResponse(savedReview))
                .build();
    }
    public ApiResponse<ReviewResponse> updateReview(String reviewId, ReviewRequest reviewRequest) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        review.setContent(reviewRequest.getContent());
        review.setRating(reviewRequest.getRating());

        Review updatedReview = reviewRepository.save(review);

        calculateAndUpdateProductRating(review.getProduct());
        return  ApiResponse.<ReviewResponse>builder()
                .message("Update review successfully")
                .code(200)
                .result(reviewMapper.toReviewResponse(updatedReview))
                .build();
    }

    public ApiResponse<List<ReviewResponse>> getReviewByProductId(String productId){
        List<Review> reviews = reviewRepository.findByProductId(productId);
        return ApiResponse.<List<ReviewResponse>>builder()
                .code(200)
                .message("Review by productId")
                .result(reviews.stream()
                        .map(reviewMapper::toReviewResponse)
                        .collect(Collectors.toList()))
                .build();
    }

    // Phương thức tính và cập nhật rating trung bình
    public void calculateAndUpdateProductRating(Product product) {
        Double averageRating = reviewRepository.calculateAverageRatingByProductId(product.getId());
        product.setRating(averageRating);
        productRepository.save(product);
    }
}
