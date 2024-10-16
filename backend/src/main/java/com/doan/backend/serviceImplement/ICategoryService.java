package com.doan.backend.serviceImplement;

import com.doan.backend.dto.request.CategoryRequest;
import com.doan.backend.dto.response.ApiResponse;
import com.doan.backend.dto.response.CategoryResponse;

import java.util.List;

public interface ICategoryService {
    ApiResponse<CategoryResponse> createCategory(CategoryRequest categoryRequest);
    ApiResponse<CategoryResponse> updateCategory(String id, CategoryRequest categoryRequest);
    //ApiResponse<CategoryResponse> updateCategory_IsActive(String id, Boolean isActive);
    ApiResponse<String> deleteCategory(String id);
    ApiResponse<CategoryResponse> getCategoryById(String id);
    ApiResponse<List<CategoryResponse>> getAllCategories();
}
