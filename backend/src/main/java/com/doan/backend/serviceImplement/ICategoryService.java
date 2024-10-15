package com.doan.backend.serviceImplement;

import com.doan.backend.dto.request.CategoryDTO;
import com.doan.backend.dto.response.ApiResponse;
import com.doan.backend.dto.response.CategoryResponse;
import com.doan.backend.entity.Category;

import java.util.List;

public interface ICategoryService {
    ApiResponse<CategoryResponse> createCategory(CategoryDTO categoryDTO);
    ApiResponse<CategoryResponse> updateCategory(String id, CategoryDTO categoryDTO);
    //ApiResponse<CategoryResponse> updateCategory_IsActive(String id, Boolean isActive);
    ApiResponse<String> deleteCategory(String id);
    ApiResponse<CategoryResponse> getCategoryById(String id);
    ApiResponse<List<CategoryResponse>> getAllCategories();
}
