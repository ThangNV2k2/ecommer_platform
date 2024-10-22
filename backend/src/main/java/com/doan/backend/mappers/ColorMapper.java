package com.doan.backend.mappers;

import com.doan.backend.dto.request.ColorRequest;
import com.doan.backend.dto.response.ColorResponse;
import com.doan.backend.entity.Color;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ColorMapper {
    ColorMapper INSTANCE = Mappers.getMapper(ColorMapper.class);
    Color toColor(ColorRequest colorRequest);
    ColorResponse toColorResponse(Color color);
}
