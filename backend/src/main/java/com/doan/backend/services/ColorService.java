package com.doan.backend.services;

import com.doan.backend.dto.request.ColorRequest;
import com.doan.backend.dto.response.ApiResponse;
import com.doan.backend.dto.response.ColorResponse;
import com.doan.backend.entity.Color;
import com.doan.backend.mapper.ColorMapper;
import com.doan.backend.repositories.ColorRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Service
public class ColorService {
    ColorMapper colorMapper;
    ColorRepository colorRepository;

    public ApiResponse<String> deleteColor(String id) {
        Optional<Color> colorOptional = colorRepository.findById(id);

        if (colorOptional.isPresent()) {
            colorRepository.deleteById(id);
            return ApiResponse.<String>builder()
                    .code(200)
                    .message("Color deleted successfully")
                    .build();
        } else {
            throw new RuntimeException("Color not found");
        }
    }

    public ApiResponse<String> updateColor(String id, ColorRequest colorRequest) {
        Color color = colorMapper.toColor(colorRequest);
        Optional<Color> colorOptional = colorRepository.findById(id);

        if (colorOptional.isPresent()) {
            Color colorToUpdate = colorOptional.get();
            colorToUpdate.setName(color.getName());
            colorRepository.save(colorToUpdate);
            return ApiResponse.<String>builder()
                    .code(200)
                    .message("Color updated successfully")
                    .build();
        } else {
            throw new RuntimeException("Color not found");
        }
    }

    public ApiResponse<ColorResponse> createColor(ColorRequest colorRequest) {
        boolean exists = colorRepository.existsByName(colorRequest.getName());

        if(exists) {
            throw new IllegalArgumentException("Color name already exists");
        }
        else {
            Color newColor = colorRepository.save(colorMapper.toColor(colorRequest));
            return ApiResponse.<ColorResponse>builder()
                    .code(200)
                    .message("Color created successfully")
                    .result(colorMapper.toColorResponse(newColor))
                    .build();
        }
    }

    public ApiResponse<List<ColorResponse>> getAllColors() {
        List<Color> colors = colorRepository.findAll();
        return ApiResponse.<List<ColorResponse>>builder()
                .code(200)
                .message("Colors retrieved successfully")
                .result(colorMapper.toColorResponseList(colors))
                .build();
    }

    public ApiResponse<ColorResponse> getColor(String id) {
        Optional<Color> colorOptional = colorRepository.findById(id);

        if (colorOptional.isPresent()) {
            return ApiResponse.<ColorResponse>builder()
                    .code(200)
                    .message("Color retrieved successfully")
                    .result(colorMapper.toColorResponse(colorOptional.get()))
                    .build();
        } else {
            throw new RuntimeException("Color not found");
        }
    }

    public ApiResponse<Page<ColorResponse>> getPageAllColors(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Color> ColorPage = colorRepository.findAll(pageable);

        Page<ColorResponse> colorResponsePage = ColorPage.map(colorMapper::toColorResponse);

        return ApiResponse.<Page<ColorResponse>>builder()
                .code(200)
                .message("Colors retrieved successfully")
                .result(colorResponsePage)
                .build();
    }
}
