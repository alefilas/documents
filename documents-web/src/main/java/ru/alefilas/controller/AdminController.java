package ru.alefilas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.alefilas.dto.permit.PermitDto;
import ru.alefilas.model.document.DocumentType;
import ru.alefilas.notification.NotificationService;
import ru.alefilas.notification.model.EmailSettings;
import ru.alefilas.notification.model.Settings;
import ru.alefilas.service.DocumentService;
import ru.alefilas.service.PermitService;

import java.util.List;
import java.util.Properties;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private PermitService permitService;

    @PostMapping("/types")
    public ResponseEntity<DocumentType> addDocumentType(@RequestBody String type) {
        return ResponseEntity.ok(documentService.save(type));
    }

    @PostMapping("/email/connection")
    public ResponseEntity<?> updateConnectionSettings(@RequestBody Properties properties) {
        notificationService.setConnectionSettings(properties);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/email/account")
    public ResponseEntity<?> updateAccountSettings(@RequestBody EmailSettings settings) {
        notificationService.setAccountSettings(settings);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/email/connection")
    public ResponseEntity<Properties> getConnectionSettings() {
        return ResponseEntity.ok(notificationService.getConnectionSettings());
    }

    @GetMapping("/email/account")
    public ResponseEntity<Settings> getAccountSettings() {
        return ResponseEntity.ok(notificationService.getAccountSettings());
    }

    @PostMapping("/permits")
    public ResponseEntity<PermitDto> savePermit(@RequestBody PermitDto dto) {
        PermitDto permitDto = permitService.save(dto);
        return ResponseEntity.ok(permitDto);
    }

    @GetMapping("/permits")
    public ResponseEntity<List<PermitDto>> getAllPermits() {
        return ResponseEntity.ok(permitService.getAllPermits());
    }

    @DeleteMapping("/permits/{id}")
    public ResponseEntity<?> deletePermitById(@PathVariable Long id) {
        permitService.delete(id);
        return ResponseEntity.ok().build();
    }
}
