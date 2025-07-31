package com.example.scheduler.service;

import com.example.scheduler.dto.SchedulerRequest;
import com.example.scheduler.dto.SchedulerResponse;
import com.example.scheduler.entity.Scheduler;
import com.example.scheduler.repository.SchedulerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

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

    // 일정 조회
    @Transactional (readOnly = true)
    public List<SchedulerResponse> findAll() {

        // Repository 에서 정보 조회
        List<Scheduler> schedules = schedulerRepository.findAll();

        // 응답 객체 리스트 초기화
        List<SchedulerResponse> schedulerResponses = new ArrayList<>();

        // 받은 정보를 Response 객체로 변환해서 저장
        for (Scheduler scheduler : schedules) {
            schedulerResponses.add(new SchedulerResponse(
                    scheduler.getId(),
                    scheduler.getTitle(),
                    scheduler.getContents(),
                    scheduler.getWriter(),
                    scheduler.getCreatedAt(),
                    scheduler.getModifiedAt()
            ));
        }

        // 조회(변환)된 리스트 반환
        return schedulerResponses;
    }

    // 선택 일정 조회
    @Transactional (readOnly = true)
    public SchedulerResponse findById(Long id) {

        // 일정 찾기 (null 값이면 NOT_FOUND)
        Scheduler schedule = schedulerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "일정을 찾을 수 없습니다"));

        // 조회된 일정 반환
        return new SchedulerResponse(
                schedule.getId(),
                schedule.getTitle(),
                schedule.getContents(),
                schedule.getWriter(),
                schedule.getCreatedAt(),
                schedule.getModifiedAt()
        );

    }
}
