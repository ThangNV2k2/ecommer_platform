package com.doan.backend.controller;

import com.doan.backend.dto.ApiResponse;
import com.doan.backend.dto.CategoryDTO;
import com.doan.backend.serviceImplement.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
    @Autowired
    private ICategoryService categoryService;

    @PostMapping
    public ApiResponse<CategoryDTO> createCategory(@RequestBody CategoryDTO categoryDTO) {
        return categoryService.createCategory(categoryDTO);
    }

    @PutMapping("/{id}")
    public ApiResponse<CategoryDTO> updateCategory(@PathVariable UUID id, @RequestBody CategoryDTO categoryDTO) {
        return categoryService.updateCategory(id, categoryDTO);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteCategory(@PathVariable UUID id) {
        return categoryService.deleteCategory(id);
    }

    @GetMapping("/{id}")
    public ApiResponse<CategoryDTO> getCategoryById(@PathVariable UUID id) {
        return categoryService.getCategoryById(id);
    }

    @GetMapping
    public ApiResponse<List<CategoryDTO>> getAllCategories() {
        return categoryService.getAllCategories();
    }
}
