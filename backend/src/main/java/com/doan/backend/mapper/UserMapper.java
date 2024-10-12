package com.doan.backend.mapper;

import com.doan.backend.dto.request.RegisterRequest;
import com.doan.backend.dto.response.UserResponse;
import com.doan.backend.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(RegisterRequest registerUser);
    UserResponse toUserResponse(User user);
    List<UserResponse> toUserResponseList(List<User> users);
}
