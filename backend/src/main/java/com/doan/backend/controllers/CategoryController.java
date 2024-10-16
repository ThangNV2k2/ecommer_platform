package com.doan.backend.controllers;

import com.doan.backend.dto.request.CategoryDTO;
import com.doan.backend.dto.response.ApiResponse;
import com.doan.backend.dto.response.CategoryResponse;
import com.doan.backend.serviceImplement.ICategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize("hasRole('ADMIN')")  // Yêu cầu xác thực toàn bộ các phương thức trong controller
@RestController
@RequestMapping("/api/category")
public class CategoryController {
    @Autowired
    private ICategoryService iCategoryService;

    @PostMapping
    public ApiResponse<CategoryResponse> createCategory(@RequestBody CategoryDTO categoryDTO) {
        return iCategoryService.createCategory(categoryDTO);
    }

    @PutMapping("/{id}")
    public ApiResponse<CategoryResponse> updateCategory(@PathVariable @Valid String id, @RequestBody CategoryDTO categoryDTO) {
        return iCategoryService.updateCategory(id, categoryDTO);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteCategory(@PathVariable @Valid String id) {
        return iCategoryService.deleteCategory(id);
    }

    @GetMapping("/{id}")
    public ApiResponse<CategoryResponse> getCategoryById(@PathVariable @Valid String id) {
        return iCategoryService.getCategoryById(id);
    }

    @GetMapping
    public ApiResponse<List<CategoryResponse>> getAllCategories() {
        return iCategoryService.getAllCategories();
    }
//    @PutMapping("/update_isActive/{id}")
//    public ApiResponse<CategoryResponse> updateCategory(@PathVariable String id, @RequestBody Boolean isActive) {
//        return _iCategoryService.updateCategory_IsActive(id, isActive);
//    }
}
