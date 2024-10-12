package com.doan.backend.serviceImplement;

import com.doan.backend.dto.ApiResponse;
import com.doan.backend.dto.CategoryDTO;
import com.doan.backend.entity.Category;
import com.doan.backend.entity.User;
import com.doan.backend.mappers.CategoryMapper;
import com.doan.backend.repositoryImplement.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CategoryService implements  ICategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public ApiResponse<CategoryDTO> createCategory(CategoryDTO categoryDTO) {
        Category category = categoryMapper.toCategory(categoryDTO);
        category = categoryRepository.save(category);
        System.out.println(category);
        return new ApiResponse<>(200, "Category created successfully", categoryMapper.toCategoryDTO(category));
    }

    @Override
    public ApiResponse<CategoryDTO> updateCategory(UUID id, CategoryDTO categoryDTO) {
        Optional<Category> existingCategory = categoryRepository.findById(id);
        if (!existingCategory.isPresent()) {
            return new ApiResponse<>(404, "Category not found", null);
        }
        Category category = existingCategory.get();
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        category = categoryRepository.save(category);
        return new ApiResponse<>(200, "Category updated successfully", categoryMapper.toCategoryDTO(category));
    }

    @Override
    public ApiResponse<Void> deleteCategory(UUID id) {
        if (!categoryRepository.existsById(id)) {
            return new ApiResponse<>(404, "Category not found", null);
        }
        categoryRepository.deleteById(id);
        return new ApiResponse<>(200, "Category deleted successfully", null);
    }

    @Override
    public ApiResponse<CategoryDTO> getCategoryById(UUID id) {
        Optional<Category> category = categoryRepository.findById(id);
        if (!category.isPresent()) {
            return new ApiResponse<>(404, "Category not found", null);
        }
        return new ApiResponse<>(200, "Category retrieved successfully", categoryMapper.toCategoryDTO(category.get()));
    }

    @Override
    public ApiResponse<List<CategoryDTO>> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryDTO> categoryDTOs = categories.stream()
                .map(categoryMapper::toCategoryDTO)
                .collect(Collectors.toList());
        return new ApiResponse<>(200, "Categories retrieved successfully", categoryDTOs);
    }
}
