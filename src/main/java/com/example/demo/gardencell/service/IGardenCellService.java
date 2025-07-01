package com.example.demo.gardencell.service;

import com.example.demo.common.ApiResponse;
import com.example.demo.gardencell.controller.dtos.*;

import java.util.List;

public interface IGardenCellService {

    /**
     * Query and return a paged/filtered view of cells for a garden.
     *
     * @param gardenId         ID of the garden to view
     * @param plantInventoryId Optional filter by plant type ID
     * @param status           Optional filter by health status
     */
    ApiResponse<GardenCellsViewResponse> findAll(
            String gardenId,
            String plantInventoryId,
            String status
    );

    ApiResponse<GardenCellsAdminResponse> adminFindAll(
            String gardenId,
            String plantInventoryId,
            String status
    );


    /**
     * Create a new garden cell.
     */
    ApiResponse<Void> createCellsBatch(String gardenId,
                                       List<CreateGardenCellRequest> requests);


    /**
     * Update an existing garden cell.
     */
    ApiResponse<Void> updateCell(UpdateGardenCellRequest request);

    /**
     * Update nhiều ô vườn cùng lúc.
     *
     * @param request Wrapper chứa gardenId và danh sách các ô cần cập nhật
     */
    ApiResponse<Void> updateCellsBatch(UpdateGardenCellsRequest request);


    /**
     * Delete one or more garden cells by ID.
     */
    ApiResponse<Void> deleteCells(DeleteGardenCellsRequest request);
}
