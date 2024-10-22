package com.doan.backend.controllers;

import com.doan.backend.dto.request.CategoryRequest;
import com.doan.backend.dto.request.ColorRequest;
import com.doan.backend.dto.response.ApiResponse;
import com.doan.backend.dto.response.CategoryResponse;
import com.doan.backend.dto.response.ColorResponse;
import com.doan.backend.serviceImplement.IColorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/color")
public class ColorController {
    @Autowired
    private IColorService colorService;

    @PostMapping
    public ApiResponse<ColorResponse> createCategory(@RequestBody ColorRequest colorRequest) {
        return colorService.createColor(colorRequest);
    }

    @PutMapping("/{id}")
    public ApiResponse<ColorResponse> updateCategory(@PathVariable @Valid String id, @RequestBody ColorRequest colorRequest) {
        return colorService.updateColor(id, colorRequest);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteCategory(@PathVariable @Valid String id) {
        return colorService.deleteColor(id);
    }

    @GetMapping("/{id}")
    public ApiResponse<ColorResponse> getCategoryById(@PathVariable @Valid String id) {
        return colorService.getColorById(id);
    }

    @GetMapping
    public ApiResponse<List<ColorResponse>> getAllCategories() {
        return colorService.getAllColors();
    }
}
