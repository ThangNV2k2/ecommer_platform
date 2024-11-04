package com.doan.backend.services;

import com.doan.backend.dto.request.DiscountRequest;
import com.doan.backend.dto.response.ApiResponse;
import com.doan.backend.entity.Discount;
import com.doan.backend.mapper.DiscountMapper;
import com.doan.backend.repositories.DiscountRepository;
import com.doan.backend.repositories.UserDiscountRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class DiscountService {

    DiscountRepository discountRepository;
    UserDiscountRepository userDiscountRepository;
    DiscountMapper discountMapper;

    public ApiResponse<Discount> createDiscount(DiscountRequest discountRequest) {
        Discount discount = discountMapper.toDiscount(discountRequest);

        if(discountRequest.getStartDate() == null) {
            discount.setStartDate(LocalDateTime.now());
        }

        if(discountRepository.existsByCode(discountRequest.getCode())) {
            throw new RuntimeException("Discount code already exists");
        }
        if(discountRequest.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Expiry date is before current date");
        }

        return ApiResponse.<Discount>builder()
                .code(200)
                .message("create created successfully")
                .result(discountRepository.save(discount))
                .build();
    }

    public ApiResponse<Discount> updateDiscount(String id, DiscountRequest discountRequest) {
        Discount discount = discountRepository.findById(id).orElseThrow(() -> new RuntimeException("Discount not found"));

        Optional.ofNullable(discountRequest.getCode()).ifPresent(discount::setCode);
        Optional.ofNullable(discountRequest.getDiscountType()).ifPresent(discount::setDiscountType);
        Optional.ofNullable(discountRequest.getDiscountPercentage()).ifPresent(discount::setDiscountPercentage);
        Optional.ofNullable(discountRequest.getDiscountValue()).ifPresent(discount::setDiscountValue);
        Optional.ofNullable(discountRequest.getMaxDiscountValue()).ifPresent(discount::setMaxDiscountValue);
        Optional.ofNullable(discountRequest.getMinOrderValue()).ifPresent(discount::setMinOrderValue);
        Optional.ofNullable(discountRequest.getMaxUses()).ifPresent(discount::setMaxUses);
        Optional.ofNullable(discountRequest.getExpiryDate()).ifPresent(discount::setExpiryDate);
        Optional.ofNullable(discountRequest.getStartDate()).ifPresent(discount::setStartDate);

        discountRepository.save(discount);

        return ApiResponse.<Discount>builder()
                .code(200)
                .message("Discount updated successfully")
                .result(discount)
                .build();
    }

    public ApiResponse<Void> deleteDiscount(String id) {
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Discount deleted  successfully")
                .build();
    }

    public ApiResponse<Iterable<Discount>> getAllDiscounts() {
        Iterable<Discount> discounts = discountRepository.findAll();
        return ApiResponse.<Iterable<Discount>>builder()
                .code(200)
                .message("Discount retrieved successfully")
                .result(discounts)
                .build();
    }

    public ApiResponse<Discount> getDiscount(String code, String userId) {
        boolean userHasUsedDiscount = userDiscountRepository.findByUserIdAndDiscount_Code(userId, code)
                .map(userDiscount -> userDiscount.getUsesCount() > 0)
                .orElse(false);

        if (userHasUsedDiscount) {
            throw new RuntimeException("User has already used this discount.");
        }

        Discount discount = discountRepository.findByCodeAndExpiryDateAfter(code, LocalDateTime.now())
                .orElseThrow(() -> new RuntimeException("Discount not found"));

        return ApiResponse.<Discount>builder()
                .code(200)
                .message("Discount retrieved successfully")
                .result(discount)
                .build();
    }

    public ApiResponse<Iterable<Discount>> getAllDiscountsByAutoApply(String userId) {
        Iterable<Discount> discounts = discountRepository.findByAutoApplyTrueAndExpiryDateAfter(LocalDateTime.now());

        Set<String> usedDiscountCodes = userDiscountRepository.findByUserId(userId).stream()
                .filter(userDiscount -> userDiscount.getUsesCount() > 0)
                .map(userDiscount -> userDiscount.getDiscount().getCode())
                .collect(Collectors.toSet());

        List<Discount> availableDiscounts = StreamSupport.stream(discounts.spliterator(), false)
                .filter(discount -> !usedDiscountCodes.contains(discount.getCode()))
                .collect(Collectors.toList());

        return ApiResponse.<Iterable<Discount>>builder()
                .code(200)
                .message("Discounts retrieved successfully")
                .result(availableDiscounts)
                .build();
    }
}
