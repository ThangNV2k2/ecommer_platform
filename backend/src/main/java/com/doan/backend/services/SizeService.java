package com.doan.backend.services;

import com.doan.backend.dto.request.SizeRequest;
import com.doan.backend.dto.response.ApiResponse;
import com.doan.backend.dto.response.SizeResponse;
import com.doan.backend.entity.Size;
import com.doan.backend.mapper.SizeMapper;
import com.doan.backend.repositories.SizeRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Service
public class SizeService {
    SizeMapper sizeMapper;
    SizeRepository sizeRepository;

    public ApiResponse<String> deleteSize(String id) {
        Optional<Size> sizeOptional = sizeRepository.findById(id);

        if (sizeOptional.isPresent()) {
            sizeRepository.deleteById(id);
            return ApiResponse.<String>builder()
                    .code(200)
                    .message("Size deleted successfully")
                    .build();
        } else {
            throw new RuntimeException("Size not found");
        }
    }

    public ApiResponse<String> updateSize(String id, SizeRequest SizeRequest) {
        Size size = sizeMapper.toSize(SizeRequest);
        Optional<Size> sizeOptional = sizeRepository.findById(id);

        if (sizeOptional.isPresent()) {
            Size sizeToUpdate = sizeOptional.get();
            sizeToUpdate.setName(size.getName());
            sizeRepository.save(sizeToUpdate);
            return ApiResponse.<String>builder()
                    .code(200)
                    .message("Size updated successfully")
                    .build();
        } else {
            throw new RuntimeException("Size not found");
        }
    }

    public ApiResponse<SizeResponse> createSize(SizeRequest sizeRequest) {
        boolean exists = sizeRepository.existsByName(sizeRequest.getName());

        if(exists) {
            throw new IllegalArgumentException("Size name already exists");
        }
        else {
            Size newSize = sizeRepository.save(sizeMapper.toSize(sizeRequest));
            return ApiResponse.<SizeResponse>builder()
                    .code(200)
                    .message("Size created successfully")
                    .result(sizeMapper.toSizeResponse(newSize))
                    .build();
        }
    }

    public ApiResponse<List<SizeResponse>> getAllSizes() {
        List<Size> Sizes = sizeRepository.findAll();
        return ApiResponse.<List<SizeResponse>>builder()
                .code(200)
                .message("Sizes retrieved successfully")
                .result(sizeMapper.toSizeResponseList(Sizes))
                .build();
    }

    public ApiResponse<SizeResponse> getSize(String id) {
        Optional<Size> sizeOptional = sizeRepository.findById(id);

        if (sizeOptional.isPresent()) {
            return ApiResponse.<SizeResponse>builder()
                    .code(200)
                    .message("Size retrieved successfully")
                    .result(sizeMapper.toSizeResponse(sizeOptional.get()))
                    .build();
        } else {
            throw new RuntimeException("Size not found");
        }
    }

    public ApiResponse<Page<SizeResponse>> getPageAllSizes(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Size> sizePage = sizeRepository.findAll(pageable);

        Page<SizeResponse> sizeResponsePage = sizePage.map(sizeMapper::toSizeResponse);

        return ApiResponse.<Page<SizeResponse>>builder()
                .code(200)
                .message("Sizes retrieved successfully")
                .result(sizeResponsePage)
                .build();
    }
}
