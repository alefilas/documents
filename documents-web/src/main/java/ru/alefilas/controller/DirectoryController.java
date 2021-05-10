package ru.alefilas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.alefilas.dto.AbstractEntityDto;
import ru.alefilas.dto.InputDirectoryDto;
import ru.alefilas.service.DirectoryService;
import ru.alefilas.dto.OutputDirectoryDto;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping(path = "/directories")
public class DirectoryController {

    @Autowired
    private DirectoryService service;

    @GetMapping("/{id}")
    public ResponseEntity<OutputDirectoryDto> getDirectoryById(@PathVariable Long id) {
        OutputDirectoryDto outputDirectoryDto = service.getDirectoryById(id);
        if (outputDirectoryDto != null) {
            return ResponseEntity.ok(outputDirectoryDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/all/{id}")
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
        try {
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No directory with id=" + id, e);
        }
    }
}
