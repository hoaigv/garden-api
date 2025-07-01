
package com.example.demo.gardenNote.repository;

import com.example.demo.gardenNote.model.GardenNoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IGardenNoteRepository
        extends JpaRepository<GardenNoteEntity, String>,
        JpaSpecificationExecutor<GardenNoteEntity> {
    // You can define custom query methods here if needed, e.g.:
    // List<GardenNoteEntity> findByGarden_Id(String gardenId);
}
