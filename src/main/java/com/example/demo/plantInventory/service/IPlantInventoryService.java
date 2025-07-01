package com.example.demo.plantInventory.service;

import com.example.demo.common.ApiResponse;
import com.example.demo.plantInventory.controllers.dtos.*;

import java.util.List;

/**
 * Service interface for managing PlantInventory entities.
 */
public interface IPlantInventoryService {

    /**
     * Retrieve a paged and filtered list of plant inventories.
     *
     * @param page      zero-based page index
     * @param size      page size
     * @param userId    optional owner user ID filter
     * @param plantType optional plant type enum filter
     * @param sortBy    field to sort by
     * @param sortDir   sort direction ("asc" or "desc")
     */
    ApiResponse<List<PlantInventoryAdminResponse>> findAll(
            Integer page,
            Integer size,
            String userId,
            String plantType,
            String sortBy,
            String sortDir
    );

    /**
     * Retrieve the full list of plant inventories owned by the currently authenticated user.
     *
     * @return ApiResponse chứa danh sách tất cả PlantInventoryResponse của user hiện tại
     */
    ApiResponse<List<PlantInventoryResponse>> findAllForCurrentUser();


    /**
     * Create a new PlantInventory entry.
     */
    ApiResponse<Void> createPlantInventory(CreatePlantInventoryRequest request);

    /**
     * Update an existing PlantInventory entry.
     */
    ApiResponse<Void> updatePlantInventory(UpdatePlantInventoryRequest request);

    /**
     * Delete one or more PlantInventory entries by ID.
     */
    ApiResponse<Void> deletePlantInventories(DeletePlantInventoriesRequest request);
}