package ru.alefilas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.alefilas.dto.moderation.ModerationResult;
import ru.alefilas.dto.moderation.ModerationTicketDto;
import ru.alefilas.service.ModerationService;

@RestController
@RequestMapping(path = "/moderation")
public class ModerationController {

    @Autowired
    private ModerationService service;

    @GetMapping("/all")
    public ResponseEntity<Page<ModerationTicketDto>> getAllTickets(@RequestParam int page) {
        Page<ModerationTicketDto> tickets = service.getAllTickets(page);
        return ResponseEntity.ok(tickets);
    }

    @PostMapping("/")
    public void moderate(@RequestBody ModerationResult result) {
        service.moderate(result);
    }
}
