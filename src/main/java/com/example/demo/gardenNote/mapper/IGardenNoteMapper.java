// src/main/java/com/example/demo/gardenNote/mapper/IGardenNoteMapper.java
package com.example.demo.gardenNote.mapper;

import com.example.demo.gardenNote.controller.dtos.CreateGardenNoteRequest;
import com.example.demo.gardenNote.controller.dtos.UpdateGardenNoteRequest;
import com.example.demo.gardenNote.controller.dtos.GardenNoteResponse;
import com.example.demo.gardenNote.model.GardenNoteEntity;
import com.example.demo.garden.model.GardenEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.MappingTarget;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Mapper(componentModel = "spring")
public interface IGardenNoteMapper {

    @Mapping(source = "noteText", target = "noteText")
    @Mapping(source = "photoUrl", target = "photoUrl")
    @Mapping(source = "gardenId", target = "garden", qualifiedByName = "idToGardenEntity")
    @Mapping(source = "noteTitle", target = "noteTitle")
    GardenNoteEntity toEntity(CreateGardenNoteRequest dto);

    @Mapping(source = "noteText", target = "noteText")
    @Mapping(source = "photoUrl", target = "photoUrl")
    @Mapping(source = "noteTitle", target = "noteTitle")
    void updateEntity(UpdateGardenNoteRequest dto, @MappingTarget GardenNoteEntity entity);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "noteText", target = "noteText")
    @Mapping(source = "photoUrl", target = "photoUrl")
    @Mapping(source = "garden", target = "gardenId", qualifiedByName = "gardenEntityToId")
    @Mapping(source = "noteTitle", target = "noteTitle")
    @Mapping(source = "createdAt", target = "createdAt", qualifiedByName = "localToOffset")
    @Mapping(source = "updatedAt", target = "updatedAt", qualifiedByName = "localToOffset")
    GardenNoteResponse toResponse(GardenNoteEntity entity);

    @Named("idToGardenEntity")
    default GardenEntity idToGardenEntity(String id) {
        if (id == null) return null;
        GardenEntity g = new GardenEntity();
        g.setId(id);
        return g;
    }


    @Named("gardenEntityToId")
    default String gardenEntityToId(GardenEntity garden) {
        return garden != null ? garden.getId() : null;
    }


    @Named("localToOffset")
    default OffsetDateTime localToOffset(LocalDateTime ldt) {
        return ldt != null ? ldt.atOffset(ZoneOffset.UTC) : null;
    }
}
