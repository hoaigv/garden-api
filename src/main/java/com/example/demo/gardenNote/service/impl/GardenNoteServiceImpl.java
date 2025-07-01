// src/main/java/com/example/demo/gardenNote/service/impl/GardenNoteServiceImpl.java
package com.example.demo.gardenNote.service.impl;

import com.example.demo.common.ApiResponse;
import com.example.demo.common.AuthUtils;
import com.example.demo.exceptions.ErrorCode;
import com.example.demo.exceptions.custom.CustomRuntimeException;
import com.example.demo.garden.model.GardenEntity;
import com.example.demo.garden.repository.GardenRepository;
import com.example.demo.garden.repository.GardenSpecification;
import com.example.demo.gardenNote.controller.dtos.CreateGardenNoteRequest;
import com.example.demo.gardenNote.controller.dtos.UpdateGardenNoteRequest;
import com.example.demo.gardenNote.controller.dtos.GardenNoteResponse;
import com.example.demo.gardenNote.mapper.IGardenNoteMapper;
import com.example.demo.gardenNote.model.GardenNoteEntity;
import com.example.demo.gardenNote.repository.IGardenNoteRepository;
import com.example.demo.gardenNote.repository.GardenNoteSpecification;
import com.example.demo.gardenNote.service.IGardenNoteService;
import com.example.demo.user.model.UserEntity;
import com.example.demo.user.repository.IUserRepository;
import com.example.demo.user.repository.UserSpecification;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GardenNoteServiceImpl implements IGardenNoteService {

    IGardenNoteRepository gardenNoteRepository;
    IGardenNoteMapper gardenNoteMapper;
    GardenRepository gardenRepository;
    IUserRepository userRepository;

    @Override
    public ApiResponse<List<GardenNoteResponse>> findAll(
            Integer page,
            Integer size,
            String gardenId,
            String userId,
            String textContains,
            OffsetDateTime fromCreatedAt,
            OffsetDateTime toCreatedAt,
            String sortBy,
            String sortDir
    ) {
        // build spec with optional filters
        Specification<GardenNoteEntity> spec = GardenNoteSpecification.build(
                null, gardenId, userId, textContains, fromCreatedAt, toCreatedAt
        );

        Sort.Direction direction = "asc".equalsIgnoreCase(sortDir)
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<GardenNoteEntity> pageResult = gardenNoteRepository.findAll(spec, pageable);

        List<GardenNoteResponse> dtos = pageResult.getContent()
                .stream()
                .map(gardenNoteMapper::toResponse)
                .collect(Collectors.toList());

        return ApiResponse.<List<GardenNoteResponse>>builder()
                .message("Successfully fetched garden notes")
                .result(dtos)
                .total(pageResult.getTotalElements())
                .totalPages(pageResult.getTotalPages())
                .build();
    }

    @Override
    public ApiResponse<List<GardenNoteResponse>> findAllForCurrentUser() {
        // get current user
        UserEntity currentUser = userRepository.findOne(
                UserSpecification.hasEmail(AuthUtils.getUserCurrent())
        ).orElseThrow(() -> new CustomRuntimeException(ErrorCode.USER_NOT_FOUND));

        Specification<GardenNoteEntity> spec = GardenNoteSpecification.hasUserId(currentUser.getId());
        List<GardenNoteResponse> dtos = gardenNoteRepository.findAll(spec)
                .stream()
                .map(gardenNoteMapper::toResponse)
                .collect(Collectors.toList());

        return ApiResponse.<List<GardenNoteResponse>>builder()
                .message("Fetched garden notes for current user")
                .result(dtos)
                .build();
    }

    @Override
    public ApiResponse<GardenNoteResponse> findGardenNoteById(String noteId) {
        GardenNoteEntity entity = gardenNoteRepository.findOne(
                GardenNoteSpecification.hasId(noteId)
        ).orElseThrow(() -> new CustomRuntimeException(ErrorCode.RESOURCE_NOT_FOUND));

        GardenNoteResponse resp = gardenNoteMapper.toResponse(entity);
        return ApiResponse.<GardenNoteResponse>builder()
                .message("Fetched garden note")
                .result(resp)
                .build();
    }

    @Override
    public ApiResponse<Void> createGardenNote(CreateGardenNoteRequest request) {
        // get current user and garden
        UserEntity currentUser = userRepository.findOne(
                UserSpecification.hasEmail(AuthUtils.getUserCurrent())
        ).orElseThrow(() -> new CustomRuntimeException(ErrorCode.USER_NOT_FOUND));

        GardenEntity garden = gardenRepository.findOne(
                GardenSpecification.hasId(request.getGardenId())
        ).orElseThrow(() -> new CustomRuntimeException(ErrorCode.GARDEN_NOT_FOUND));

        GardenNoteEntity entity = gardenNoteMapper.toEntity(request);
        entity.setUser(currentUser);
        entity.setGarden(garden);
        gardenNoteRepository.save(entity);

        return ApiResponse.<Void>builder()
                .message("Garden note created successfully")
                .build();
    }

    @Override
    public ApiResponse<Void> updateGardenNote(UpdateGardenNoteRequest request) {
        GardenNoteEntity existing = gardenNoteRepository.findById(request.getId())
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.RESOURCE_NOT_FOUND));

        gardenNoteMapper.updateEntity(request, existing);
        gardenNoteRepository.save(existing);

        return ApiResponse.<Void>builder()
                .message("Garden note updated successfully")
                .build();
    }

    @Override
    public ApiResponse<Void> deleteGardenNotes(List<String> noteIds) {
        for (String id : noteIds) {
            if (gardenNoteRepository.existsById(id)) {
                gardenNoteRepository.deleteById(id);
            } else {
                throw new CustomRuntimeException(ErrorCode.RESOURCE_NOT_FOUND);
            }
        }
        return ApiResponse.<Void>builder()
                .message("Garden notes deleted successfully")
                .build();
    }
}
