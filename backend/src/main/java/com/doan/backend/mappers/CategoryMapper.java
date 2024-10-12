package com.doan.backend.mappers;
import com.doan.backend.dto.CategoryDTO;
import com.doan.backend.entity.Category;
import com.doan.backend.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);
    Category toCategory(CategoryDTO categoryDTO);
    CategoryDTO toCategoryDTO(Category category);
}
