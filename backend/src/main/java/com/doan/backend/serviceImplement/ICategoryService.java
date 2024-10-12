package com.doan.backend.serviceImplement;

import com.doan.backend.dto.ApiResponse;
import com.doan.backend.dto.CategoryDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

public interface ICategoryService {
    ApiResponse<CategoryDTO> createCategory(CategoryDTO categoryDTO);
    ApiResponse<CategoryDTO> updateCategory(UUID id, CategoryDTO categoryDTO);
    ApiResponse<Void> deleteCategory(UUID id);
    ApiResponse<CategoryDTO> getCategoryById(UUID id);
    ApiResponse<List<CategoryDTO>> getAllCategories();
}
