package com.doan.backend.services;


import com.doan.backend.dto.request.ShippingAddressRequest;
import com.doan.backend.dto.response.ApiResponse;
import com.doan.backend.dto.response.ShippingAddressResponse;
import com.doan.backend.entity.ShippingAddress;
import com.doan.backend.mapper.ShippingAddressMapper;
import com.doan.backend.repositories.ShippingAddressRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class ShippingAddressService {

    ShippingAddressRepository shippingAddressRepository;
    ShippingAddressMapper shippingAddressMapper;

    public ApiResponse<Void> createShippingAddress(ShippingAddressRequest shippingAddressRequest) {
        if(shippingAddressRequest.getIsDefault() && shippingAddressRepository.existsByUserIdAndIsDefault(shippingAddressRequest.getUserId(), true)) {
            throw new RuntimeException("Default shipping address already exists");
        }
        ShippingAddress shippingAddress = shippingAddressMapper.toShippingAddress(shippingAddressRequest);
        shippingAddressRepository.save(shippingAddress);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Create shipping address successfully")
                .build();
    }

    public ApiResponse<Void> updateShippingAddress(String id, ShippingAddressRequest shippingAddressRequest) {
        ShippingAddress shippingAddress = shippingAddressRepository.findById(id).orElseThrow(() -> new RuntimeException("Shipping address not found"));
        if(shippingAddress.getIsDefault() && shippingAddressRequest.getIsDefault() && shippingAddressRepository.existsByUserIdAndIsDefault(shippingAddressRequest.getUserId(), true)) {
            throw new RuntimeException("Default shipping address already exists");
        }
        shippingAddress.setRecipientName(shippingAddressRequest.getRecipientName());
        shippingAddress.setPhoneNumber(shippingAddressRequest.getPhoneNumber());
        shippingAddress.setAddressDetail(shippingAddressRequest.getAddressDetail());
        shippingAddress.setCountry(shippingAddressRequest.getCountry());
        shippingAddress.setIsDefault(shippingAddressRequest.getIsDefault());
        shippingAddressRepository.save(shippingAddress);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Update shipping address successfully")
                .build();
    }

    public ApiResponse<Iterable<ShippingAddressResponse>> getShippingAddressByUserId(String userId) {
        Iterable<ShippingAddressResponse> shippingAddresses = shippingAddressMapper.toShippingAddressResponse(shippingAddressRepository.findByUserId(userId));
        return ApiResponse.<Iterable<ShippingAddressResponse>>builder()
                .code(200)
                .message("Get shipping address by user id successfully")
                .result(shippingAddresses)
                .build();
    }

    public ApiResponse<Void> deleteShippingAddress(String id) {
        shippingAddressRepository.deleteById(id);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Delete shipping address successfully")
                .build();
    }
}
