package com.example.scheduler.dto;

import lombok.Getter;

@Getter
public class SchedulerRequest {

    private String title;
    private String content;
    private String writer;
    private String password;
}
