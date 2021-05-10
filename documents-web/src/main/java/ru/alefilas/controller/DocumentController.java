package ru.alefilas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.alefilas.dto.InputDocumentDto;
import ru.alefilas.dto.InputDocumentVersionDto;
import ru.alefilas.dto.OutputDocumentVersionDto;
import ru.alefilas.model.document.DocumentVersion;
import ru.alefilas.service.DocumentService;
import ru.alefilas.dto.OutputDocumentDto;
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
        if (documentDto != null) {
            return ResponseEntity.ok(documentDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/types")
    public ResponseEntity<List<DocumentType>> getAllTypes() {
        List<DocumentType> types = service.getAllDocumentTypes();
        return ResponseEntity.ok(types);
    }

    @PostMapping("/")
    public ResponseEntity<OutputDocumentDto> addDocument(@RequestBody InputDocumentDto documentDto) {
        OutputDocumentDto savedDto = service.save(documentDto);
        return ResponseEntity.ok(savedDto);
    }

    @PostMapping("/{id}/versions")
    public ResponseEntity<OutputDocumentVersionDto> addVersion(@RequestBody InputDocumentVersionDto version, @PathVariable Long id) {
        return ResponseEntity.ok(service.save(version, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDocumentById(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
