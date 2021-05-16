package ru.alefilas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.alefilas.dto.documents.InputDocumentDto;
import ru.alefilas.dto.documents.InputDocumentVersionDto;
import ru.alefilas.dto.documents.OutputDocumentVersionDto;
import ru.alefilas.service.DocumentService;
import ru.alefilas.dto.documents.OutputDocumentDto;
import ru.alefilas.model.document.DocumentType;

import java.util.List;


@RestController
@RequestMapping(path = "/documents")
public class DocumentController {

    @Autowired
    private DocumentService service;

    @GetMapping("/{id}")
    public ResponseEntity<OutputDocumentDto> getDocumentById(@PathVariable Long id) {
        OutputDocumentDto documentDto = service.getDocumentById(id);
        return ResponseEntity.ok(documentDto);
    }

    @GetMapping("/types")
    public ResponseEntity<List<DocumentType>> getAllTypes() {
        List<DocumentType> types = service.getAllDocumentTypes();
        return ResponseEntity.ok(types);
    }

    @GetMapping("/{id}/versions")
    public ResponseEntity<List<OutputDocumentVersionDto>> getDocumentVersionsById(@PathVariable Long id) {
        List<OutputDocumentVersionDto> versions = service.getDocumentVersionsById(id);
        return ResponseEntity.ok(versions);
    }

    @GetMapping("/all")
    public ResponseEntity<Page<OutputDocumentDto>> getAllDocuments(@RequestParam int page) {
        Page<OutputDocumentDto> documents = service.getAllDocuments(page);
        return ResponseEntity.ok(documents);
    }

    @GetMapping("/all/type")
    public ResponseEntity<Page<OutputDocumentDto>> getDocumentsByType(@RequestParam int page, @RequestParam String type) {
        Page<OutputDocumentDto> documents = service.getDocumentsByType(type, page);
        return ResponseEntity.ok(documents);
    }

    @PostMapping
    public ResponseEntity<OutputDocumentDto> addDocument(@RequestBody InputDocumentDto documentDto) {
        OutputDocumentDto savedDto = service.save(documentDto);
        return ResponseEntity.ok(savedDto);
    }

    @PostMapping("/{id}/versions")
    public ResponseEntity<OutputDocumentVersionDto> addVersion(@RequestBody InputDocumentVersionDto version, @PathVariable Long id) {
        OutputDocumentVersionDto versionDto = service.save(version, id);
        return ResponseEntity.ok(versionDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDocumentById(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
