package com.doan.backend.services;

import com.doan.backend.dto.request.PromotionRequest;
import com.doan.backend.dto.response.ApiResponse;
import com.doan.backend.dto.response.PromotionResponse;
import com.doan.backend.entity.Promotion;
import com.doan.backend.mapper.PromotionMapper;
import com.doan.backend.repositories.PromotionProductRepository;
import com.doan.backend.repositories.PromotionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class PromotionService {
    PromotionRepository promotionRepository;
    PromotionProductRepository promotionProductRepository;
    PromotionMapper promotionMapper;

    public ApiResponse<PromotionResponse> createPromotion(PromotionRequest promotionRequest) {
        Promotion promotion = promotionMapper.toPromotion(promotionRequest);
        promotion = promotionRepository.save(promotion);
        return ApiResponse.<PromotionResponse>builder()
                .code(200)
                .message("Promotion created successfully")
                .result(promotionMapper.toPromotionResponse(promotion))
                .build();
    }

    public ApiResponse<PromotionResponse> updatePromotion(String id, PromotionRequest promotionRequest) {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Promotion not found"));

        promotion.setName(promotionRequest.getName());
        promotion.setDescription(promotionRequest.getDescription());
        promotion.setStartDate(promotionRequest.getStartDate());
        promotion.setEndDate(promotionRequest.getEndDate());
        promotion.setApplyToAll(promotionRequest.getApplyToAll());
        promotion.setIsActive(promotionRequest.getIsActive());

        promotionRepository.save(promotion);
        return ApiResponse.<PromotionResponse>builder()
                .code(200)
                .message("Promotion update successfully")
                .result(promotionMapper.toPromotionResponse(promotion))
                .build();
    }

    public ApiResponse<Void> deletePromotion(String id) {
        promotionRepository.deleteById(id);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Promotion deleted successfully")
                .build();
    }

    public ApiResponse<PromotionResponse> getPromotionById(String id) {
        PromotionResponse promotionResponse = promotionRepository.findById(id)
                .map(promotionMapper::toPromotionResponse)
                .orElseThrow(() -> new RuntimeException("Promotion not found"));
        return ApiResponse.<PromotionResponse>builder()
                .code(200)
                .message("Promotion retrieved successfully")
                .result(promotionResponse)
                .build();
    }
}
