package ru.alefilas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.alefilas.DocumentService;
import ru.alefilas.dto.DocumentDto;
import ru.alefilas.model.document.DocumentType;

import java.util.List;


@RestController
public class DocumentController {

    @Autowired
    private DocumentService service;

    @GetMapping("documents/{id}")
    public ResponseEntity<DocumentDto> getDocumentById(@PathVariable Long id) {
        DocumentDto documentDto = service.getDocumentById(id);
        if (documentDto != null) {
            return ResponseEntity.ok(documentDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("documents/types")
    public ResponseEntity<List<DocumentType>> getAllTypes() {
        List<DocumentType> types = service.getAllDocumentTypes();
        return ResponseEntity.ok(types);
    }

    @PostMapping("documents")
    public ResponseEntity<DocumentDto> addDocument(@RequestBody DocumentDto documentDto) {
        DocumentDto savedDto = service.save(documentDto);
        return ResponseEntity.ok(savedDto);
    }

    @DeleteMapping("documents/{id}")
    public ResponseEntity<?> deleteDocumentById(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
