package com.doan.backend.mappers;

import com.doan.backend.dto.request.SizeRequest;
import com.doan.backend.dto.response.SizeResponse;
import com.doan.backend.entity.Size;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface SizeMapper {
    SizeMapper INSTANCE = Mappers.getMapper(SizeMapper.class);
    Size toSize(SizeRequest sizeRequest);
    SizeResponse toSizeResponse(Size size);
}
