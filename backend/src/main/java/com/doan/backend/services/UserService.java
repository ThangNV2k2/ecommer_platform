package com.doan.backend.services;

import com.doan.backend.dto.response.ApiResponse;
import com.doan.backend.dto.response.UserResponse;
import com.doan.backend.entity.User;
import com.doan.backend.mapper.UserMapper;
import com.doan.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;

    public ApiResponse<Page<UserResponse>> getAllUser(String name, Pageable pageable) {

        Page<UserResponse> userResponses = userRepository.findByNameContainingIgnoreCase(name, pageable).map(userMapper::toUserResponse);
        return ApiResponse.<Page<UserResponse>>builder()
                .code(200)
                .message("Get all user successfully")
                .result(userResponses)
                .build();
    }

    public ApiResponse<UserResponse> getUserById(String id) {
        UserResponse userResponse = userMapper.toUserResponse(userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found")));
        return ApiResponse.<UserResponse>builder()
                .code(200)
                .message("Get user successfully")
                .result(userResponse)
                .build();
    }

    public ApiResponse<Void> deleteUser(String id) {
        userRepository.deleteById(id);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Delete user successfully")
                .build();
    }

    public ApiResponse<List<UserResponse>> getAll() {
        List<User> users = userRepository.findAll();

        return ApiResponse.<List<UserResponse>>builder()
                .code(200)
                .message("Get user successfully")
                .result(userMapper.toUserResponseList(users))
                .build();
    }
}
