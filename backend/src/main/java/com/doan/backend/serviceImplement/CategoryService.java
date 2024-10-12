package com.doan.backend.serviceImplement;

import com.doan.backend.dto.request.CategoryDTO;
import com.doan.backend.dto.response.ApiResponse;
import com.doan.backend.dto.response.CategoryResponse;
import com.doan.backend.entity.Category;
import com.doan.backend.mappers.CategoryMapper;
import com.doan.backend.repositoryImplement.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService implements  ICategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public ApiResponse<CategoryResponse> createCategory(CategoryDTO categoryDTO) {
        if(isNameExist(categoryDTO.getName())){
            return new ApiResponse<>(404, "CategoryName exist",null);
        }
        Category category = categoryMapper.toCategory(categoryDTO);
        category = categoryRepository.save(category);
        return new ApiResponse<>(200, "Category created successfully", categoryMapper.toCategoryResponse(category));
    }

    @Override
    public ApiResponse<CategoryResponse> updateCategory(String id, CategoryDTO categoryDTO) {
        Optional<Category> existingCategory = categoryRepository.findById(id);
        if (!existingCategory.isPresent()) {
            return new ApiResponse<>(404, "Category not found", null);
        }
        if(isNameExist(categoryDTO.getName())){
            return new ApiResponse<>(404, "CategoryName exist",null);
        }

        Category category = existingCategory.get();
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        category = categoryRepository.save(category);
        return new ApiResponse<>(200, "Category updated successfully", categoryMapper.toCategoryResponse(category));
    }

//    @Override
//    public ApiResponse<CategoryResponse> updateCategory_IsActive(String id, Boolean isActive) {
//        Optional<Category> existingCategory = categoryRepository.findById(id);
//        System.out.println(existingCategory);
//        if (!existingCategory.isPresent()) {
//            return new ApiResponse<>(404, "Category not found", null);
//        }
//
//        Category category = existingCategory.get();
//        category.setIsActive(isActive);
//        category = categoryRepository.save(category);
//        category.getIsActive();
//        return new ApiResponse<>(200, "Category updated successfully", categoryMapper.toCategoryResponse(category));
//    }

    @Override
    public ApiResponse<Void> deleteCategory(String id) {
        if (!categoryRepository.existsById(id)) {
            return new ApiResponse<>(404, "Category not found", null);
        }
        categoryRepository.deleteById(id);
        return new ApiResponse<>(200, "Category deleted successfully", null);
    }

    @Override
    public ApiResponse<CategoryResponse> getCategoryById(String id) {
        Optional<Category> category = categoryRepository.findById(id);
        if (!category.isPresent()) {
            return new ApiResponse<>(404, "Category not found", null);
        }
        return new ApiResponse<>(200, "Category retrieved successfully", categoryMapper.toCategoryResponse(category.get()));
    }

    @Override
    public ApiResponse<List<CategoryResponse>> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryResponse> categoryDTOs = categories.stream()
                .map(categoryMapper::toCategoryResponse)
                .collect(Collectors.toList());
        return new ApiResponse<>(200, "Categories retrieved successfully", categoryDTOs);
    }

    public boolean isNameExist(String name) {
        return categoryRepository.findByName(name).isPresent();
    }
}
