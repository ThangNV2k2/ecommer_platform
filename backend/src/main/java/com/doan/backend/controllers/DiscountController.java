package com.doan.backend.controllers;

import com.doan.backend.dto.request.DiscountRequest;
import com.doan.backend.dto.response.ApiResponse;
import com.doan.backend.entity.Discount;
import com.doan.backend.services.DiscountService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/discounts")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class DiscountController {

    DiscountService discountService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Discount> createDiscount(@RequestBody @Validated DiscountRequest discountRequest) {
        return discountService.createDiscount(discountRequest);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ApiResponse<Discount> updateDiscount(@PathVariable String id, @RequestBody @Validated DiscountRequest discountRequest) {
        return discountService.updateDiscount(id, discountRequest);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteDiscount(@PathVariable String id) {
        return discountService.deleteDiscount(id);
    }

    @GetMapping("/getCode")
    public ApiResponse<Discount> getDiscountByCode(@RequestParam String code, @RequestParam String userId) {
        return discountService.getDiscount(code, userId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ApiResponse<Iterable<Discount>> getAllDiscounts() {
        return discountService.getAllDiscounts();
    }

    @GetMapping("/auto-apply")
    public ApiResponse<Iterable<Discount>> getAllDiscountsByAutoApply(@RequestParam String userId) {
        return discountService.getAllDiscountsByAutoApply(userId);
    }
}
