package ru.alefilas.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.alefilas.service.DirectoryService;
import ru.alefilas.service.DocumentService;
import ru.alefilas.dto.DirectoryDto;
import ru.alefilas.dto.EntityDto;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping(path = "/directories")
public class DirectoryController {

    @Autowired
    private DirectoryService service;

    @GetMapping("/{id}")
    public ResponseEntity<DirectoryDto> getDirectoryById(@PathVariable Long id) {
        DirectoryDto directoryDto = service.getDirectoryById(id);
        if (directoryDto != null) {
            return ResponseEntity.ok(directoryDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/all/{id}")
    public ResponseEntity<List<EntityDto>> getDirectoryData(@PathVariable Long id) {
        return ResponseEntity.ok(service.getEntitiesByDirectory(service.getDirectoryById(id)));
    }

    @PostMapping("/")
    public ResponseEntity<DirectoryDto> addDirectory(@RequestBody DirectoryDto directoryDto) {
        DirectoryDto savedDto = service.save(directoryDto);
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
