package com.doan.backend.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;

@Validated
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
public class PhoneRequest {

    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Phone number is invalid")
    String phoneNumber;
}
