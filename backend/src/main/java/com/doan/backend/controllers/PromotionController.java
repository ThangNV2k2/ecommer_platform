package com.doan.backend.controllers;

import com.doan.backend.dto.request.PromotionRequest;
import com.doan.backend.dto.response.ApiResponse;
import com.doan.backend.dto.response.PromotionResponse;
import com.doan.backend.services.PromotionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/promotion")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class PromotionController {
    PromotionService promotionService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ApiResponse<PromotionResponse> createPromotion(@RequestBody PromotionRequest promotionRequest) {
        return promotionService.createPromotion(promotionRequest);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ApiResponse<PromotionResponse> updatePromotion(
            @PathVariable String id,
            @RequestBody PromotionRequest promotionRequest) {
        return promotionService.updatePromotion(id, promotionRequest);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deletePromotion(@PathVariable String id) {
        return promotionService.deletePromotion(id);

    }

    @GetMapping("/{id}")
    public ApiResponse<PromotionResponse> getPromotionById(@PathVariable String id) {
        return promotionService.getPromotionById(id);
    }

}
