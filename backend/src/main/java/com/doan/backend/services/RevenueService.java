package com.doan.backend.services;

import com.doan.backend.dto.response.ApiResponse;
import com.doan.backend.dto.response.ProductRevenueResponse;
import com.doan.backend.repositories.OrderItemRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class RevenueService {
    OrderItemRepository orderItemRepository;

    public ApiResponse<List<ProductRevenueResponse>> getRevenueByProductAndDate(LocalDate date) {
        return ApiResponse.<List<ProductRevenueResponse>>builder()
                .code(200)
                .message("Thong ke doanh thu san pham theo ngay")
                .result(orderItemRepository.findRevenueByProductAndDate(date))
                .build();
    }

    public ApiResponse<List<ProductRevenueResponse>> getRevenueByProductAndWeek(LocalDate date) {
        return ApiResponse.<List<ProductRevenueResponse>>builder()
                .code(200)
                .message("Thong ke doanh thu theo tuan")
                .result(orderItemRepository.findRevenueByProductAndWeek(date))
                .build();
    }

    public ApiResponse<List<ProductRevenueResponse>> getRevenueByProductAndMonth(int month, int year) {
        return ApiResponse.<List<ProductRevenueResponse>>builder()
                .code(200)
                .message("Thong ke doanh thu theo thang")
                .result(orderItemRepository.findRevenueByProductAndMonth(month, year))
                .build();
    }

    public ApiResponse<List<ProductRevenueResponse>> getRevenueByProductAndQuarter(int quarter, int year) {
        return ApiResponse.<List<ProductRevenueResponse>>builder()
                .code(200)
                .message("Thong ke doanh thu theo quy")
                .result(orderItemRepository.findRevenueByProductAndQuarter(quarter, year))
                .build();
    }

    public ApiResponse<List<ProductRevenueResponse>> getRevenueByProductAndYear(int year) {
        return ApiResponse.<List<ProductRevenueResponse>>builder()
                .code(200)
                .message("Thong ke doanh thu theo nam")
                .result(orderItemRepository.findRevenueByProductAndYear(year))
                .build();
    }

//    public ApiResponse<List<ProductRevenueResponse>> getRevenueByProductBetweenDates(LocalDateTime startDate, LocalDateTime endDate){
//        return ApiResponse.<List<ProductRevenueResponse>>builder()
//                .code(200)
//                .message("Thong ke doanh thu trong khoang thoi gian chon")
//                .result(orderItemRepository.findRevenueByProductBetweenDates(startDate, endDate))
//                .build();
//    }
}
