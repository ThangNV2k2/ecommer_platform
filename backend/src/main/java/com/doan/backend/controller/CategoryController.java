package com.doan.backend.controller;

import com.doan.backend.dto.request.CategoryDTO;
import com.doan.backend.dto.response.ApiResponse;
import com.doan.backend.dto.response.CategoryResponse;
import com.doan.backend.serviceImplement.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
    @Autowired
    private ICategoryService _iCategoryService;

    @PostMapping
    public ApiResponse<CategoryResponse> createCategory(@RequestBody CategoryDTO categoryDTO) {
        return _iCategoryService.createCategory(categoryDTO);
    }

    @PutMapping("/{id}")
    public ApiResponse<CategoryResponse> updateCategory(@PathVariable String id, @RequestBody CategoryDTO categoryDTO) {
        return _iCategoryService.updateCategory(id, categoryDTO);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteCategory(@PathVariable String id) {
        return _iCategoryService.deleteCategory(id);
    }

    @GetMapping("/{id}")
    public ApiResponse<CategoryResponse> getCategoryById(@PathVariable String id) {
        return _iCategoryService.getCategoryById(id);
    }

    @GetMapping
    public ApiResponse<List<CategoryResponse>> getAllCategories() {
        return _iCategoryService.getAllCategories();
    }
//    @PutMapping("/update_isActive/{id}")
//    public ApiResponse<CategoryResponse> updateCategory(@PathVariable String id, @RequestBody Boolean isActive) {
//        return _iCategoryService.updateCategory_IsActive(id, isActive);
//    }
}
