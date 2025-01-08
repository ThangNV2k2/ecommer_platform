package com.doan.backend.services;

import com.doan.backend.config.JwtTokenProvider;
import com.doan.backend.dto.request.LoginEmailRequest;
import com.doan.backend.dto.request.RegisterRequest;
import com.doan.backend.dto.request.ResetPassword;
import com.doan.backend.dto.response.ApiResponse;
import com.doan.backend.dto.response.JwtResponse;
import com.doan.backend.dto.response.UserResponse;
import com.doan.backend.entity.User;
import com.doan.backend.enums.RoleEnum;
import com.doan.backend.enums.StatusEnum;
import com.doan.backend.exception.Unauthorized;
import com.doan.backend.mapper.UserMapper;
import com.doan.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Service
public class AuthService {

    EmailService emailService;
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    JwtTokenProvider jwtTokenProvider;
    UserMapper userMapper;

    @NonFinal
    @Value("${app.base-url}")
    String baseUrl;

    @NonFinal
    @Value("${app.client-url}")
    String clientUrl;

    public ApiResponse<JwtResponse> loginWithEmail(LoginEmailRequest loginEmailRequest) {
        User user = userRepository.findByEmail(loginEmailRequest.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + loginEmailRequest.getEmail()));

        if (!passwordEncoder.matches(loginEmailRequest.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        if (user.getStatus().equals(StatusEnum.INACTIVE)) {
            throw new BadCredentialsException("Account is not activated");
        }

        if (user.getStatus().equals(StatusEnum.DELETED)) {
            throw new BadCredentialsException("Account is deleted");
        }

        String token = jwtTokenProvider.generateToken(user.getEmail(), Map.of("roles", user.getRoles()));
        JwtResponse jwtResponse = new JwtResponse(token, userMapper.toUserResponse(user));

        return ApiResponse.<JwtResponse>builder()
                .code(200)
                .message("Login successful")
                .result(jwtResponse)
                .build();
    }

    public ApiResponse<UserResponse> registerWithEmail(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new IllegalArgumentException("Email is already taken");
        }

        String verificationToken = UUID.randomUUID().toString();

        User newUser = User.builder()
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .name(registerRequest.getName())
                .roles(registerRequest.getRoles() != null ? registerRequest.getRoles() : Set.of(RoleEnum.CUSTOMER))
                .status(StatusEnum.INACTIVE)
                .verificationToken(verificationToken)
                .build();

        User savedUser = userRepository.save(newUser);

        String verificationUrl = clientUrl + "/auth/verify?token=" + verificationToken;
        String emailContent = "<p>Chào bạn,</p>"
                + "<p>Cảm ơn bạn đã đăng ký. Vui lòng nhấn vào liên kết bên dưới để kích hoạt tài khoản của bạn:</p>"
                + "<p><a href=\"" + verificationUrl + "\" style=\"color: blue; text-decoration: underline;\">Tại đây</a></p>"
                + "<p>Nếu bạn không yêu cầu đăng ký tài khoản, hãy bỏ qua email này.</p>";
        emailService.sendVerificationEmail(registerRequest.getEmail(), "Verify your account", emailContent);

        UserResponse userResponse = userMapper.toUserResponse(savedUser);

        return ApiResponse.<UserResponse>builder()
                .code(201)
                .message("Please verify your email")
                .result(userResponse)
                .build();
    }

    public ApiResponse<String> sendOtp(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        String otp = String.valueOf((int) ((Math.random() * (999999 - 100000)) + 100000));
        user.setOtp(otp);
        userRepository.save(user);

        String emailContent = "<p>Mã OTP của bạn là: <strong>" + otp + "</strong></p>"
                + "<p>Mã này sẽ hết hạn sau 5 phút.</p>";
        emailService.sendVerificationEmail(email, "OTP Verification", emailContent);

        return ApiResponse.<String>builder()
                .code(200)
                .message("OTP sent successfully")
                .result(user.getId())
                .build();
    }

    public ApiResponse<String> resetPassword(ResetPassword resetPassword) {
        User user = userRepository.findByEmail(resetPassword.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + resetPassword.getEmail()));

        if (!user.getOtp().equals(resetPassword.getOtp())) {
            throw new IllegalArgumentException("Invalid OTP");
        }

        user.setPassword(passwordEncoder.encode(resetPassword.getPassword()));
        user.setOtp(null);
        userRepository.save(user);

        return ApiResponse.<String>builder()
                .code(200)
                .message("Password changed successfully")
                .result(user.getId())
                .build();
    }

    public ApiResponse<String> verifyAccount(String token) {
        User user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid verification token"));

        user.setStatus(StatusEnum.ACTIVE);
        user.setVerificationToken(null);
        userRepository.save(user);

        return ApiResponse.<String>builder()
                .code(200)
                .result(user.getId())
                .message("Account verified successfully")
                .build();
    }

    public ApiResponse<UserResponse> getUser() {
        return ApiResponse.<UserResponse>builder()
                .code(200)
                .result(userMapper.toUserResponse(getUserByToken()))
                .build();

    }

    public User getUserByToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            return userRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + userDetails.getUsername()));
        } else {
            throw new Unauthorized("Unauthorized access");
        }
    }

    public User getChatBotUser() {
        List<User> users = userRepository.findByRoles(RoleEnum.CHATBOT);

        if (!users.isEmpty()) {
            return users.getFirst();
        } else {
            throw new RuntimeException("Chatbot user not found");
        }
    }
}
