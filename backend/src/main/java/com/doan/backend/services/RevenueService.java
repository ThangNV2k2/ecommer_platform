package com.doan.backend.services;

import com.doan.backend.dto.response.*;
import com.doan.backend.dto.response.CustomerStatistic.CustomerRevenueResponse;
import com.doan.backend.dto.response.CustomerStatistic.CustomerStatistic;
import com.doan.backend.dto.response.CustomerStatistic.CustomerStatisticResponse;
import com.doan.backend.repositories.OrderItemRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public ApiResponse<List<ProductRevenueResponse>> getProductRevenue(LocalDate startDate, LocalDate endDate) {
        List<ProductStatisticResponse> statisticsData = orderItemRepository.getProductRevenue(startDate.atStartOfDay(), endDate.atStartOfDay());

        return ApiResponse.<List<ProductRevenueResponse>>builder()
                .result(statisticsData.stream()
                        .collect(Collectors.groupingBy(ProductStatisticResponse::getProductName))
                        .entrySet()
                        .stream()
                        .map(entry -> new ProductRevenueResponse(entry.getKey(), entry.getValue()))
                        .collect(Collectors.toList())
                )
                .build();
    }

    public ApiResponse<List<CategoryRevenueResponse>> getCategoryRevenue(LocalDate startDate, LocalDate endDate) {
        List<CategoryStatisticResponse> statisticsData = orderItemRepository.getCategoryRevenue(startDate.atStartOfDay(), endDate.atStartOfDay());

        return ApiResponse.<List<CategoryRevenueResponse>>builder()
                .result(statisticsData.stream()
                        .collect(Collectors.groupingBy(CategoryStatisticResponse::getCategoryName))
                        .entrySet()
                        .stream()
                        .map(entry -> new CategoryRevenueResponse(entry.getKey(), entry.getValue()))
                        .collect(Collectors.toList())
                )
                .build();
    }

    public ApiResponse<List<CustomerStatisticResponse>> getCustomerRevenue(LocalDate startDate, LocalDate endDate){
        List<CustomerStatisticResponse> responses = orderItemRepository.getCustomerRevenue(startDate.atStartOfDay(),endDate.atStartOfDay());
        Map<String,CustomerStatisticResponse> responseMap = new HashMap<>();

        //for(Map<String, Object> entry : responses)
        return ApiResponse.<List<CustomerStatisticResponse>>builder()
                .result(responses)
                .build();
    }

    public List<CustomerRevenueResponse> formatUserData(List<Map<String, Object>> data) {
        // Sử dụng Map để nhóm dữ liệu theo userId
        Map<String, CustomerRevenueResponse> responseMap = new HashMap<>();

        for (Map<String, Object> entry : data) {
            String userId = (String) entry.get("userId");
            String name = (String) entry.get("name");
            String email = (String) entry.get("email");
            String orderId = (String) entry.get("orderId");
            Double value = (Double) entry.get("value");
            LocalDateTime date = (LocalDateTime) entry.get("date");

            // Kiểm tra xem user đã có trong map chưa
            CustomerRevenueResponse customerRevenueResponse = responseMap.get(userId);
            if (customerRevenueResponse == null) {
                customerRevenueResponse = new CustomerRevenueResponse();
                customerRevenueResponse.setUserId(userId);
                customerRevenueResponse.setName(name);
                customerRevenueResponse.setEmail(email);
                responseMap.put(userId, customerRevenueResponse);
            }

            // Thêm đơn hàng vào statistics
            CustomerStatistic customerStatistic = new CustomerStatistic(orderId, value, date);
            customerRevenueResponse.getStatistics().add(customerStatistic);

            // Cập nhật tổng số đơn hàng và tổng giá trị
            customerRevenueResponse.setTotalOrder(customerRevenueResponse.getTotalOrder() + 1);
            customerRevenueResponse.setTotalValue(customerRevenueResponse.getTotalValue() + value);
        }

        // Trả về danh sách sau khi đã nhóm và tính toán
        return new ArrayList<>(responseMap.values());
    }
}
