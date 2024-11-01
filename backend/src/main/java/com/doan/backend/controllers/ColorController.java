package com.doan.backend.controllers;

import com.doan.backend.dto.request.ColorRequest;
import com.doan.backend.dto.response.ApiResponse;
import com.doan.backend.dto.response.ColorResponse;
import com.doan.backend.services.ColorService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/color")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PUBLIC, makeFinal = true)
public class ColorController {
    ColorService colorService;

    @GetMapping("/{id}")
    ApiResponse<ColorResponse> getColor(@PathVariable String id) {
        return colorService.getColor(id);
    }

    @GetMapping
    ApiResponse<List<ColorResponse>> getAllColors() {
        return colorService.getAllColors();
    }

    @GetMapping("/page")
    ApiResponse<Page<ColorResponse>> getPageAllColors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return colorService.getPageAllColors(page, size);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    ApiResponse<ColorResponse> createColor(
            @RequestBody ColorRequest colorRequest) {
        return colorService.createColor(colorRequest);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    ApiResponse<String> updateColor(
            @PathVariable String id,
            @RequestBody ColorRequest colorRequest) {
        return colorService.updateColor(id, colorRequest);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    ApiResponse<String> deleteColor(@PathVariable String id) {
        return colorService.deleteColor(id);
    }
}
