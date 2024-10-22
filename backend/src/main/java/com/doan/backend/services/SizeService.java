package com.doan.backend.services;

import com.doan.backend.dto.request.SizeRequest;
import com.doan.backend.dto.response.ApiResponse;
import com.doan.backend.dto.response.SizeResponse;
import com.doan.backend.dto.response.SizeResponse;
import com.doan.backend.entity.Size;
import com.doan.backend.entity.Size;
import com.doan.backend.mappers.SizeMapper;
import com.doan.backend.repositoryImplement.SizeRepository;
import com.doan.backend.serviceImplement.ISizeService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class SizeService implements ISizeService {
    @Autowired
    private SizeRepository sizeRepository;
    @Autowired
    private SizeMapper sizeMapper;


    @Override
    public ApiResponse<SizeResponse> createSize(SizeRequest sizeRequest) {
        if(isNameExist(sizeRequest.getName())){
            throw new IllegalArgumentException(sizeRequest.getName()+"already existed!");
        }

        Size size = Size.builder()
                .name(sizeRequest.getName())
                .build();
        Size saveSize = sizeRepository.save(size);

        // Return ApiResponse
        return ApiResponse.<SizeResponse>builder()
                .code(200)
                .message("Size created successfully")
                .result(sizeMapper.toSizeResponse(saveSize))
                .build();
    }

    @Override
    public ApiResponse<SizeResponse> updateSize(String id, SizeRequest sizeRequest) {
        Optional<Size> infoSize = sizeRepository.findById(id);
        if (!infoSize.isPresent()) {
            throw new IllegalArgumentException(id+" does not exist");
        }
        if(isNameExist(sizeRequest.getName())){
            throw new IllegalArgumentException(sizeRequest.getName()+"already existed!");
        }

        Size size = infoSize.get();
        size.setName(sizeRequest.getName());
        size = sizeRepository.save(size);

        return  ApiResponse.<SizeResponse>builder()
                .code(200)
                .message("Size updated successfully")
                .result(sizeMapper.toSizeResponse(size))
                .build();
    }

    @Override
    public ApiResponse<String> deleteSize(String id) {
        if (!sizeRepository.existsById(id)) {
            throw new IllegalArgumentException(id+" does not exist");
        }
        sizeRepository.deleteById(id);
        return ApiResponse.<String>builder()
                .code(200)
                .message("Size deleted successfully")
                .result(id)
                .build();
    }

    @Override
    public ApiResponse<SizeResponse> getSizeById(String id) {
        Optional<Size> infoSize = sizeRepository.findById(id);
        if(!infoSize.isPresent()){
            throw new IllegalArgumentException(id+" does not exist");
        }

        return ApiResponse.<SizeResponse>builder()
                .code(200)
                .message("")
                .result(sizeMapper.toSizeResponse(infoSize.get()))
                .build();
    }

    @Override
    public ApiResponse<List<SizeResponse>> getAllSizes() {
        List<Size> sizes = sizeRepository.findAll();
        List<SizeResponse> SizeDTOs = sizes.stream()
                .map(sizeMapper::toSizeResponse)
                .collect(Collectors.toList());
        return new ApiResponse<>(200, "Sizes retrieved successfully", SizeDTOs);
    }

    public boolean isNameExist(String name) {
        return sizeRepository.findByName(name).isPresent();
    }
}
