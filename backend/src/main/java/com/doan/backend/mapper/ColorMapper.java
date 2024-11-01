package com.doan.backend.mapper;

import com.doan.backend.dto.request.ColorRequest;
import com.doan.backend.dto.response.ColorResponse;
import com.doan.backend.entity.Color;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ColorMapper {
    Color toColor(ColorRequest colorRequest);
    ColorResponse toColorResponse(Color color);
    List<ColorResponse> toColorResponseList(List<Color> colors);
}
