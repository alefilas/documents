package ru.alefilas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.alefilas.dto.documents.AbstractEntityDto;
import ru.alefilas.dto.documents.InputDirectoryDto;
import ru.alefilas.service.DirectoryService;
import ru.alefilas.dto.documents.OutputDirectoryDto;

import java.util.List;

@RestController
@RequestMapping(path = "/directories")
public class DirectoryController {

    @Autowired
    private DirectoryService service;

    @GetMapping("/{id}")
    public ResponseEntity<OutputDirectoryDto> getDirectoryById(@PathVariable Long id) {
        OutputDirectoryDto outputDirectoryDto = service.getDirectoryById(id);
        return ResponseEntity.ok(outputDirectoryDto);

    }

    @GetMapping("/{id}/all")
    @Transactional
    public ResponseEntity<List<AbstractEntityDto>> getDirectoryData(@PathVariable Long id) {
        return ResponseEntity.ok(service.getEntitiesByDirectoryId(id));
    }

    @PostMapping("/")
    public ResponseEntity<OutputDirectoryDto> addDirectory(@RequestBody InputDirectoryDto inputDirectoryDto) {
        OutputDirectoryDto savedDto = service.save(inputDirectoryDto);
        return ResponseEntity.ok(savedDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDirectoryById(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
