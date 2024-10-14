package com.doan.backend.dto.request;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;

@Validated
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
public class LoginEmailRequest {
    @Email
    @NotEmpty
    String email;

    @NotEmpty
    String password;
}
