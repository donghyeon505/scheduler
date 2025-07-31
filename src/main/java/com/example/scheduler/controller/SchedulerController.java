package com.example.scheduler.controller;

import com.example.scheduler.dto.SchedulerRequest;
import com.example.scheduler.dto.SchedulerResponse;
import com.example.scheduler.service.SchedulerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/schedules")
public class SchedulerController {

    private final SchedulerService schedulerService;

    // 일정 등록
    @PostMapping
    public SchedulerResponse createSchedule(@RequestBody SchedulerRequest schedulerRequest) {
        return schedulerService.save(schedulerRequest);
    }
}
