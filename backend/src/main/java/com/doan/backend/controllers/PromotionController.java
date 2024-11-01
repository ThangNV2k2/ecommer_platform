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
@RequestMapping("/promtion")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@FieldDefaults(level = AccessLevel.PUBLIC, makeFinal = true)
public class PromotionController {
    PromotionService promotionService;

    @GetMapping("/{id}")
    ApiResponse<PromotionResponse> getPromotion(@PathVariable String id){return promotionService.getPromotionById(id);}

    @PostMapping
    ApiResponse<PromotionResponse> createPromotion(@RequestBody PromotionRequest promotionRequest){
        return promotionService.createPromotion(promotionRequest);
    }

    @PutMapping("/{id}")
    ApiResponse<PromotionResponse> updatePromotion(@PathVariable String id, @RequestBody PromotionRequest promotionRequest){
        return promotionService.updatePromotion(id,promotionRequest);
    }

    @DeleteMapping("/{id}")
    ApiResponse<Void> deletePromotion(@PathVariable String id){
        return promotionService.deletePromotion(id);
    }


}
