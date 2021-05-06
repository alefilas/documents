package ru.alefilas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.alefilas.DocumentService;
import ru.alefilas.dto.DirectoryDto;
import ru.alefilas.dto.EntityDto;

import java.util.List;

@RestController
public class DirectoryController {

    @Autowired
    private DocumentService service;

    @GetMapping("directories/{id}")
    public ResponseEntity<DirectoryDto> getDirectoryById(@PathVariable Long id) {
        DirectoryDto directoryDto = service.getDirectoryById(id);
        if (directoryDto != null) {
            return ResponseEntity.ok(directoryDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("directories/all/{id}")
    public ResponseEntity<List<EntityDto>> getDirectoryData(@PathVariable Long id) {
        return ResponseEntity.ok(service.getEntitiesByDirectory(service.getDirectoryById(id)));
    }

    @PostMapping("directories")
    public ResponseEntity<DirectoryDto> addDirectory(@RequestBody DirectoryDto directoryDto) {
        DirectoryDto savedDto = service.save(directoryDto);
        return ResponseEntity.ok(savedDto);
    }

    @DeleteMapping("directories/{id}")
    public ResponseEntity<?> deleteDirectoryById(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}