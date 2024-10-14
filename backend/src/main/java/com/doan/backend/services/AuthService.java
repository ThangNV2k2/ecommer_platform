package com.doan.backend.services;

import com.doan.backend.config.JwtTokenProvider;
import com.doan.backend.dto.request.LoginEmailRequest;
import com.doan.backend.dto.request.RegisterRequest;
import com.doan.backend.dto.response.ApiResponse;
import com.doan.backend.dto.response.JwtResponse;
import com.doan.backend.dto.response.UserResponse;
import com.doan.backend.entity.User;
import com.doan.backend.enums.RoleEnum;
import com.doan.backend.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import com.doan.backend.mapper.UserMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@AllArgsConstructor
@Service
public class AuthService {

    EmailService emailService;
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    JwtTokenProvider jwtTokenProvider;
    UserMapper userMapper;

    // Đăng nhập bằng email và password
    public ApiResponse<JwtResponse> loginWithEmail(LoginEmailRequest loginEmailRequest) {
        User user = userRepository.findByEmail(loginEmailRequest.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + loginEmailRequest.getEmail()));

        // Kiểm tra mật khẩu
        if (!passwordEncoder.matches(loginEmailRequest.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        if (!user.getIsActive()) {
            throw new BadCredentialsException("Account is not activated");
        }

        // Tạo JWT cho người dùng
        String token = jwtTokenProvider.generateToken(user.getEmail(), Map.of("roles", user.getRoles()));
        JwtResponse jwtResponse = new JwtResponse(token);

        // Trả về ApiResponse
        return ApiResponse.<JwtResponse>builder()
                .code(200)
                .message("Login successful")
                .result(jwtResponse)
                .build();
    }

    // Đăng nhập bằng Google OAuth
    public ApiResponse<JwtResponse> loginWithGoogle(String token) throws Exception {
        // Giải mã token nhận từ NextAuth.js
        String jwtToken = token.replace("Bearer ", "");
        if (!jwtTokenProvider.validateToken(jwtToken)) {
            throw new Exception("Invalid Google token");
        }

        String email = jwtTokenProvider.getEmailFromToken(jwtToken);

        // Kiểm tra xem người dùng có trong cơ sở dữ liệu không
        Optional<User> existingUser = userRepository.findByEmail(email);
        User user;
        if (existingUser.isPresent()) {
            user = existingUser.get();
            if (!user.getIsActive()) {
                throw new BadCredentialsException("Account has been locked");
            }
        } else {
            // Tạo tài khoản mới nếu người dùng chưa tồn tại
            user = new User();
            user.setEmail(email);
            user.setGoogleId(jwtTokenProvider.getClaimsFromToken(jwtToken).get("sub").toString());
            user.setName(jwtTokenProvider.getClaimsFromToken(jwtToken).get("name").toString());
            user.setRoles(Set.of(RoleEnum.CUSTOMER));  // Gán vai trò mặc định
            user.setIsActive(true);
            userRepository.save(user);
        }

        // Tạo JWT mới cho người dùng
        String newToken = jwtTokenProvider.generateToken(user.getEmail(), Map.of("roles", user.getRoles()));
        JwtResponse jwtResponse = new JwtResponse(newToken);

        // Trả về ApiResponse
        return ApiResponse.<JwtResponse>builder()
                .code(200)
                .message("Google login successful")
                .result(jwtResponse)
                .build();
    }

    // Đăng ký bằng email/password
    public ApiResponse<UserResponse> registerWithEmail(RegisterRequest registerRequest) {
        // Kiểm tra xem email đã tồn tại chưa
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new IllegalArgumentException("Email is already taken");
        }

        String verificationToken = UUID.randomUUID().toString();

        // Tạo tài khoản mới
        User newUser = User.builder()
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword())) // Mã hóa mật khẩu
                .name(registerRequest.getName())
                .roles(registerRequest.getRoles() != null ? registerRequest.getRoles() : Set.of(RoleEnum.CUSTOMER)) // Vai trò mặc định là CUSTOMER nếu không được cung cấp
                .isActive(false)
                .verificationToken(verificationToken)
                .build();

        // Lưu vào cơ sở dữ liệu
        User savedUser = userRepository.save(newUser);

        String verificationUrl = "http://localhost:8080/auth/verify?token=" + verificationToken;
        emailService.sendVerificationEmail(registerRequest.getEmail(), "Verify your account", verificationUrl);

        // Chuyển đổi sang UserResponse
        UserResponse userResponse = userMapper.toUserResponse(savedUser);

        // Trả về ApiResponse
        return ApiResponse.<UserResponse>builder()
                .code(201)
                .message("User registered successfully")
                .result(userResponse)
                .build();
    }

    public ApiResponse<String> verifyAccount(String token) {
        User user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid verification token"));

        // Kích hoạt tài khoản
        user.setIsActive(true);
        user.setVerificationToken(null); // Xóa token sau khi xác thực
        userRepository.save(user);

        return ApiResponse.<String>builder()
                .code(200)
                .message("Account verified successfully")
                .build();
    }

    public ApiResponse<UserResponse> getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            User user = userRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + userDetails.getUsername()));
            return ApiResponse.<UserResponse>builder()
                    .code(200)
                    .result(userMapper.toUserResponse(user))
                    .build();
        } else {
            throw new BadCredentialsException("User not authenticated");
        }
    }
}

