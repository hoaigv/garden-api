package com.example.demo.gardenHealth.controller.dto;

import jakarta.persistence.Column;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GardenHealthResponse {

    String id;

    String gardenId;

    Integer normalCell;


    Integer deadCell;


    Integer diseaseCell;

    String healthStatus;


    String diseaseName;
    LocalDateTime createdAt;
}
