package com.doan.backend.services;

import com.doan.backend.dto.request.CategoryDTO;
import com.doan.backend.dto.response.ApiResponse;
import com.doan.backend.dto.response.CategoryResponse;
import com.doan.backend.dto.response.JwtResponse;
import com.doan.backend.entity.Category;
import com.doan.backend.mappers.CategoryMapper;
import com.doan.backend.repositoryImplement.CategoryRepository;
import com.doan.backend.serviceImplement.ICategoryService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class CategoryService implements ICategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);

    @Override
    public ApiResponse<CategoryResponse> createCategory(CategoryDTO categoryDTO) {
        if (isNameExist(categoryDTO.getName())){
            throw new IllegalArgumentException(categoryDTO.getName()+"already existed!");
        }
        Category category = categoryMapper.toCategory(categoryDTO);
        category = categoryRepository.save(category);
        //return new ApiResponse<>(200, "Category created successfully", categoryMapper.toCategoryResponse(category));

        // Trả về ApiResponse
        return ApiResponse.<CategoryResponse>builder()
                .code(200)
                .message("Category created successfully")
                .result(categoryMapper.toCategoryResponse(category))
                .build();

    }

    @Override
    public ApiResponse<CategoryResponse> updateCategory(String id, CategoryDTO categoryDTO) {
        Optional<Category> infoCategory = categoryRepository.findById(id);
        if (!infoCategory.isPresent()) {
            throw new IllegalArgumentException(id+" does not exist");
        }
        if(isNameExist(categoryDTO.getName())){
            throw new IllegalArgumentException(categoryDTO.getName()+"already existed!");
        }

        Category category = infoCategory.get();
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        category = categoryRepository.save(category);
        //return new ApiResponse<>(200, "Category updated successfully", categoryMapper.toCategoryResponse(category));
        // Trả về ApiResponse
        return ApiResponse.<CategoryResponse>builder()
                .code(200)
                .message("Category updated successfully")
                .result(categoryMapper.toCategoryResponse(category))
                .build();
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
    public ApiResponse<String> deleteCategory(String id) {
        if (!categoryRepository.existsById(id)) {
            throw new IllegalArgumentException(id+" does not exist");
        }
        categoryRepository.deleteById(id);
        //return new ApiResponse<>(200, "Category deleted successfully", null);
        return ApiResponse.<String>builder()
                .code(200)
                .message("Category deleted successfully")
                .result(id)
                .build();
    }

    @Override
    public ApiResponse<CategoryResponse> getCategoryById(String id) {
        Optional<Category> infoCategory = categoryRepository.findById(id);
        if (!infoCategory.isPresent()) {
            throw new IllegalArgumentException(id+" does not exist");
        }
       // return new ApiResponse<>(200, "Category retrieved successfully", categoryMapper.toCategoryResponse(category.get()));
        return ApiResponse.<CategoryResponse>builder()
                .code(200)
                .message("Category deleted successfully")
                .result(categoryMapper.toCategoryResponse(infoCategory.get()))
                .build();
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
