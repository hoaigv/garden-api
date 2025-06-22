package com.example.demo.garden.service;

import com.example.demo.common.ApiResponse;
import com.example.demo.garden.controller.dtos.CreateGardenRequest;
import com.example.demo.garden.controller.dtos.DeleteGardensRequest;
import com.example.demo.garden.controller.dtos.GardenResponse;
import com.example.demo.garden.controller.dtos.UpdateGardenRequest;

import java.util.List;

public interface IGardenService {

    /**
     * Retrieve a paged and filtered list of gardens.
     *
     * @param page      zero-based page index
     * @param size      page size
     * @param userId    optional owner user ID filter
     * @param name      optional garden name filter (contains)
     * @param condition optional garden condition filter
     * @param minRows   optional minimum rowLength filter
     * @param maxRows   optional maximum rowLength filter
     * @param minCols   optional minimum colLength filter
     * @param maxCols   optional maximum colLength filter
     * @param sortBy    field to sort by
     * @param sortDir   sort direction ("asc" or "desc")
     */
    ApiResponse<List<GardenResponse>> findAll(
            Integer page,
            Integer size,
            String userId,
            String name,
            String condition,
            Integer minRows,
            Integer maxRows,
            Integer minCols,
            Integer maxCols,
            String sortBy,
            String sortDir
    );


    /**
     * Retrieve the full list of gardens owned by the currently authenticated user.
     * <p>
     * Không có phân trang, sắp xếp hay filter theo userId truyền vào:
     * userId sẽ được lấy trực tiếp từ Spring Security Context.
     *
     * @return ApiResponse chứa danh sách tất cả các GardenResponse
     */
    ApiResponse<List<GardenResponse>> findAllForCurrentUser();


    /**
     * Retrieve a single garden by its ID.
     */
    ApiResponse<GardenResponse> findById(String id);

    /**
     * Create a new garden.
     */
    ApiResponse<GardenResponse> createGarden(CreateGardenRequest request);

    /**
     * Update an existing garden.
     */
    ApiResponse<Void> updateGarden(UpdateGardenRequest request);

    /**
     * Delete one or more gardens by ID.
     */
    ApiResponse<Void> deleteGardens(DeleteGardensRequest request);
}
