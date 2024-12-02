package com.doan.backend.services;

import com.doan.backend.dto.response.ApiResponse;
import com.doan.backend.dto.response.ProductRevenueResponse;
import com.doan.backend.repositories.OrderItemRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class RevenueService {
    OrderItemRepository orderItemRepository;

    @Transactional
    public ApiResponse<List<ProductRevenueResponse>> getRevenueProductByDay(LocalDate startDate, LocalDate endDate) {
        return ApiResponse.<List<ProductRevenueResponse>>builder()
                .code(200)
                .message("Thong ke doanh thu san pham theo ngay")
                .result(orderItemRepository.getRevenueByDate(Date.valueOf(startDate), Date.valueOf(endDate)))
                .build();
    }

//    public ApiResponse<List<ProductRevenueResponse>> getRevenueByProductAndWeek(LocalDate date) {
//        return ApiResponse.<List<ProductRevenueResponse>>builder()
//                .code(200)
//                .message("Thong ke doanh thu theo tuan")
//                .result(orderItemRepository.findRevenueByProductAndWeek(date))
//                .build();
//    }
//
    @Transactional
    public ApiResponse<List<ProductRevenueResponse>> getRevenueProductByMonth(int month, int year) {
        return ApiResponse.<List<ProductRevenueResponse>>builder()
                .code(200)
                .message("Thong ke doanh thu theo thang")
                .result(orderItemRepository.getRevenueByMonth(month, year))
                .build();
    }

    @Transactional
    public ApiResponse<List<ProductRevenueResponse>> getRevenueProductByQuarter(int quarter, int year) {
        return ApiResponse.<List<ProductRevenueResponse>>builder()
                .code(200)
                .message("Thong ke doanh thu theo quy")
                .result(orderItemRepository.GetRevenueProductByQuarter(quarter, year))
                .build();
    }

    @Transactional
    public ApiResponse<List<ProductRevenueResponse>> getRevenueProductByYear(int year) {
        return ApiResponse.<List<ProductRevenueResponse>>builder()
                .code(200)
                .message("Thong ke doanh thu theo nam")
                .result(orderItemRepository.GetRevenueProductByYear(year))
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
