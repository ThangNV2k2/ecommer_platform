package com.doan.backend.serviceImplement;

import com.doan.backend.dto.request.ColorRequest;
import com.doan.backend.dto.response.ApiResponse;
import com.doan.backend.dto.response.ColorResponse;

import java.util.List;

public interface IColorService {
    ApiResponse<ColorResponse> createColor(ColorRequest colorRequest);
    ApiResponse<ColorResponse> updateColor(String id, ColorRequest colorRequest);
    ApiResponse<String> deleteColor(String id);
    ApiResponse<ColorResponse> getColorById(String id);
    ApiResponse<List<ColorResponse>> getAllColors();
}
