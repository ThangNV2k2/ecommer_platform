package com.doan.backend.services;

import com.doan.backend.dto.request.PromotionRequest;
import com.doan.backend.dto.response.ApiResponse;
import com.doan.backend.dto.response.PromotionResponse;
import com.doan.backend.entity.Promotion;
import com.doan.backend.mapper.PromotionMapper;
import com.doan.backend.repositories.PromotionRepository;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@AllArgsConstructor
public class PromotionService {
    PromotionRepository promotionRepository;
    PromotionMapper promotionMapper;

    public ApiResponse<PromotionResponse> createPromotion(PromotionRequest promotionRequest) {
        boolean existPromtion = promotionRepository.existsByName(promotionRequest.getName());
        if(existPromtion) {
            throw new IllegalArgumentException("Promotion name already exists");
        }
        Promotion promotion = promotionMapper.toPromotion(promotionRequest);
        promotion = promotionRepository.save(promotion);
        return ApiResponse.<PromotionResponse>builder()
                .code(200)
                .message("Promotion created successfully")
                .result(promotionMapper.toPromotionResponse(promotion))
                .build();
    }

    public ApiResponse<PromotionResponse> updatePromotion(String id, PromotionRequest promotionRequest){
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Promotion not found"));

        promotion.setName(promotionRequest.getName());
        promotion.setDescription(promotionRequest.getDescription());
        promotion.setStartDate(promotionRequest.getStartDate());
        promotion.setEndDate(promotionRequest.getEndDate());
        promotion.setIsGlobal(promotionRequest.getIsGlobal());
        promotion.setIsActive(promotionRequest.getIsActive());

        promotionRepository.save(promotion);
        return  ApiResponse.<PromotionResponse>builder()
                .code(200)
                .message("Promotion update successfully")
                .result(promotionMapper.toPromotionResponse(promotion))
                .build();
    }

    public ApiResponse<Void> deletePromotion(String id){
        promotionRepository.deleteById(id);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Promotion deleted successfully")
                .build();
    }

    public ApiResponse<PromotionResponse> getPromotionById(String id){
        PromotionResponse promotionResponse = promotionRepository.findById(id)
                .map(promotionMapper::toPromotionResponse)
                .orElseThrow(()-> new RuntimeException("Promtion not found"));
        return ApiResponse.<PromotionResponse>builder()
                .code(200)
                .message("Promotion retrieved successfully")
                .result(promotionResponse)
                .build();
    }
}
