package com.doan.backend.controllers;

import com.doan.backend.dto.response.ApiResponse;
import com.doan.backend.dto.response.ProductRevenueResponse;
import com.doan.backend.services.RevenueService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/revenue")
@PreAuthorize("hasRole('ADMIN')")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class RevenueController {
    RevenueService revenueService;

    @GetMapping("/product/day")
    public ApiResponse<List<ProductRevenueResponse>> getRevenueProductByDay(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return revenueService.getRevenueProductByDay(startDate,endDate);
    }

//    @GetMapping("/product/week")
//    public ApiResponse<List<ProductRevenueResponse>> getRevenueByProductAndWeek(
//            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
//        return revenueService.getRevenueByProductAndWeek(date);
//    }
//
    @GetMapping("/product/month")
    public ApiResponse<List<ProductRevenueResponse>> getRevenueProductByMonth(
            @RequestParam("month") int month, @RequestParam("year") int year) {
        return revenueService.getRevenueProductByMonth(month, year);
    }

    @GetMapping("/product/quarter")
    public ApiResponse<List<ProductRevenueResponse>> getRevenueProductByQuarter(
            @RequestParam("quarter") int quarter, @RequestParam("year") int year) {
        return revenueService.getRevenueProductByQuarter(quarter, year);
    }

    @GetMapping("/product/year")
    public ApiResponse<List<ProductRevenueResponse>> getRevenueProductByYear(@RequestParam("year") int year) {
        return revenueService.getRevenueProductByYear(year);
    }

//    @GetMapping("/product/range")
//    public ApiResponse<List<ProductRevenueResponse>> getRevenueByProductBetweenDates(
//            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
//            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
//        return revenueService.getRevenueByProductBetweenDates(startDate, endDate);
//    }
}
