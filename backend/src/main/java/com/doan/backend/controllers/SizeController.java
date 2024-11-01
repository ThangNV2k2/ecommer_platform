package com.doan.backend.controllers;

import com.doan.backend.dto.request.SizeRequest;
import com.doan.backend.dto.response.ApiResponse;
import com.doan.backend.dto.response.SizeResponse;
import com.doan.backend.services.SizeService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/size")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PUBLIC, makeFinal = true)
public class SizeController {
    SizeService sizeService;

    @GetMapping("/{id}")
    ApiResponse<SizeResponse> getSize(@PathVariable String id) {
        return sizeService.getSize(id);
    }

    @GetMapping
    ApiResponse<List<SizeResponse>> getAllSizes() {
        return sizeService.getAllSizes();
    }

    @GetMapping("/page")
    ApiResponse<Page<SizeResponse>> getPageAllSizes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return sizeService.getPageAllSizes(page, size);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    ApiResponse<SizeResponse> createSize(
            @RequestBody SizeRequest sizeRequest) {
        return sizeService.createSize(sizeRequest);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    ApiResponse<String> updateSize(
            @PathVariable String id,
            @RequestBody SizeRequest sizeRequest) {
        return sizeService.updateSize(id, sizeRequest);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    ApiResponse<String> deleteSize(@PathVariable String id) {
        return sizeService.deleteSize(id);
    }
}
