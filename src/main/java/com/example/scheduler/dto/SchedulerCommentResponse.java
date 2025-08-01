package com.example.scheduler.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class SchedulerCommentResponse {
    public final SchedulerResponse scheduler;
    public final List<CommentResponse> comments;
}
