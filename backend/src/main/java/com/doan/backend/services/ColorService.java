package com.doan.backend.services;

import com.doan.backend.dto.request.ColorRequest;
import com.doan.backend.dto.response.ApiResponse;
import com.doan.backend.dto.response.ColorResponse;
import com.doan.backend.entity.Color;
import com.doan.backend.mappers.ColorMapper;
import com.doan.backend.repositoryImplement.ColorRepository;
import com.doan.backend.serviceImplement.IColorService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ColorService implements IColorService {
    @Autowired
    private ColorRepository colorRepository;

    @Autowired
    private ColorMapper colorMapper;

    @Override
    public ApiResponse<ColorResponse> createColor(ColorRequest colorRequest) {
        if(isNameExist(colorRequest.getName())){
            throw new IllegalArgumentException(colorRequest.getName()+"already existed!");
        }

        Color color = Color.builder()
                .name(colorRequest.getName())
                .build();
        Color saveColor = colorRepository.save(color);

        // Return ApiResponse
        return ApiResponse.<ColorResponse>builder()
                .code(200)
                .message("Color created successfully")
                .result(colorMapper.toColorResponse(saveColor))
                .build();
    }

    @Override
    public ApiResponse<ColorResponse> updateColor(String id, ColorRequest colorRequest) {
        Optional<Color> infoColor = colorRepository.findById(id);
        if (!infoColor.isPresent()) {
            throw new IllegalArgumentException(id+" does not exist");
        }
        if(isNameExist(colorRequest.getName())){
            throw new IllegalArgumentException(colorRequest.getName()+"already existed!");
        }

        Color color = infoColor.get();
        color.setName(colorRequest.getName());
        color = colorRepository.save(color);

        return  ApiResponse.<ColorResponse>builder()
                .code(200)
                .message("Color updated successfully")
                .result(colorMapper.toColorResponse(color))
                .build();
    }

    @Override
    public ApiResponse<String> deleteColor(String id) {
        if (!colorRepository.existsById(id)) {
            throw new IllegalArgumentException(id+" does not exist");
        }
        colorRepository.deleteById(id);
        return ApiResponse.<String>builder()
                .code(200)
                .message("Color deleted successfully")
                .result(id)
                .build();
    }

    @Override
    public ApiResponse<ColorResponse> getColorById(String id) {
        Optional<Color> infoColor = colorRepository.findById(id);
        if(!infoColor.isPresent()){
            throw new IllegalArgumentException(id+" does not exist");
        }

        return ApiResponse.<ColorResponse>builder()
                .code(200)
                .message("")
                .result(colorMapper.toColorResponse(infoColor.get()))
                .build();
    }

    @Override
    public ApiResponse<List<ColorResponse>> getAllColors() {
        List<Color> colors = colorRepository.findAll();
        List<ColorResponse> colorDTOs = colors.stream()
                .map(colorMapper::toColorResponse)
                .collect(Collectors.toList());
        return new ApiResponse<>(200, "Colors retrieved successfully", colorDTOs);
    }

    public boolean isNameExist(String name) {
        return colorRepository.findByName(name).isPresent();
    }
}
