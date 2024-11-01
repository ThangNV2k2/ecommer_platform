package com.doan.backend.mapper;

import com.doan.backend.dto.request.SizeRequest;
import com.doan.backend.dto.response.SizeResponse;
import com.doan.backend.entity.Size;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SizeMapper {
    Size toSize(SizeRequest sizeRequest);
    SizeResponse toSizeResponse(Size size);
    List<SizeResponse> toSizeResponseList(List<Size> sizes);
}
