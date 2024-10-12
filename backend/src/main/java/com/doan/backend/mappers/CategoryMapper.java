package com.doan.backend.mappers;
import com.doan.backend.dto.request.CategoryDTO;
import com.doan.backend.dto.response.CategoryResponse;
import com.doan.backend.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);
    Category toCategory(CategoryDTO categoryDTO);
    CategoryResponse toCategoryResponse(Category category);
}
