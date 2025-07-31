package com.example.scheduler.service;

import com.example.scheduler.dto.SchedulerRequest;
import com.example.scheduler.dto.SchedulerResponse;
import com.example.scheduler.entity.Scheduler;
import com.example.scheduler.repository.SchedulerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SchedulerService {

    private final SchedulerRepository schedulerRepository;

    // 일정 등록
    @Transactional
    public SchedulerResponse save(SchedulerRequest schedulerRequest) {

        // scheduler 객체 생성
        Scheduler scheduler = new Scheduler(
                schedulerRequest.getTitle(),
                schedulerRequest.getContents(),
                schedulerRequest.getWriter(),
                schedulerRequest.getPassword()
        );
        Scheduler savedScheduler = schedulerRepository.save(scheduler);

        // 저장된 일정 정보 반환 (비밀번호 제외)
        return new SchedulerResponse(
                savedScheduler.getId(),
                savedScheduler.getTitle(),
                savedScheduler.getContents(),
                savedScheduler.getWriter(),
                savedScheduler.getCreatedAt(),
                savedScheduler.getModifiedAt()
        );
    }
}
