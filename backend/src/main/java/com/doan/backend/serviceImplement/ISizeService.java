package com.doan.backend.serviceImplement;

import com.doan.backend.dto.request.SizeRequest;
import com.doan.backend.dto.response.ApiResponse;
import com.doan.backend.dto.response.SizeResponse;

import java.util.List;

public interface ISizeService {
    ApiResponse<SizeResponse> createSize(SizeRequest sizeRequest);
    ApiResponse<SizeResponse> updateSize(String id, SizeRequest sizeRequest);
    ApiResponse<String> deleteSize(String id);
    ApiResponse<SizeResponse> getSizeById(String id);
    ApiResponse<List<SizeResponse>> getAllSizes();
}
