package com.example.scheduler.controller;

import com.example.scheduler.dto.CommentRequest;
import com.example.scheduler.dto.CommentResponse;
import com.example.scheduler.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/schedules/{id}/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponse addComment(@PathVariable Long id, @RequestBody CommentRequest commentRequest) {
        return commentService.addComment(id, commentRequest);
    }
}
