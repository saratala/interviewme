package com.interviewme.controller;

import com.interviewme.domain.InterviewSession;
import com.interviewme.service.InterviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/interviews")
public class InterviewController {
    private final InterviewService interviewService;

    public InterviewController(InterviewService interviewService) {
        this.interviewService = interviewService;
    }

    @PostMapping
    public ResponseEntity<InterviewSession> createSession(@RequestParam String name, @RequestParam String email) {
        return ResponseEntity.ok(interviewService.createSession(name, email));
    }
}