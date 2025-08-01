package com.example.scheduler.service;

import com.example.scheduler.dto.CommentResponse;
import com.example.scheduler.dto.SchedulerCommentResponse;
import com.example.scheduler.dto.SchedulerRequest;
import com.example.scheduler.dto.SchedulerResponse;
import com.example.scheduler.entity.Comment;
import com.example.scheduler.entity.Scheduler;
import com.example.scheduler.repository.CommentRepository;
import com.example.scheduler.repository.SchedulerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SchedulerService {

    private final SchedulerRepository schedulerRepository;
    private final CommentRepository commentRepository;

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

        // DB에 저장
        Scheduler savedScheduler = schedulerRepository.save(scheduler);

        // 저장된 일정 정보 반환 (비밀번호 제외)
        return toResponse(savedScheduler);
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
            schedulerResponses.add(toResponse(scheduler));
        }

        // 조회(변환)된 리스트 반환
        return schedulerResponses;
    }

    // 선택 일정 조회
    @Transactional (readOnly = true)
    public SchedulerCommentResponse findById(Long id) {

        // 일정 찾기
        Scheduler schedule = find(id);

        // 일정 Response로 변환
        SchedulerResponse schedules = toResponse(schedule);

        // 댓글 조회 하기
        List<Comment> comments = commentRepository.findAll();

        // 응답 객체 리스트 초기화
        List<CommentResponse> commentResponses = new ArrayList<>();

        // 받은 정보를 Response 객체로 변환해서 저장 (해당 일정 아이디 값만)
        for (Comment comment : comments) {
            if (comment.getSchedulerId().equals(id)) {
                commentResponses.add(new CommentResponse(
                        comment.getComment(),
                        comment.getWriter(),
                        comment.getCreatedAt(),
                        comment.getModifiedAt(),
                        comment.getSchedulerId())
                );
            }
        }

        // 조회된 일정과 댓글 반환
        return new SchedulerCommentResponse(schedules, commentResponses);

    }

    // 일정 수정
    @Transactional
    public SchedulerResponse updateSchedule(@PathVariable Long id, SchedulerRequest schedulerRequest) {

        // 일정 찾기
        Scheduler schedule = find(id);

        // 비밀번호 체크
        checkPassword(schedulerRequest, schedule);

        // 필요 값 체크
        if (schedulerRequest.getTitle() == null || schedulerRequest.getContents() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 요청입니다");
        }

        // 일정 제목, 내용 수정
        schedule.setTitle(schedulerRequest.getTitle());
        schedule.setContents(schedulerRequest.getContents());

        // 수정된 값 DB에 반영
        schedulerRepository.flush();

        // 수정된 내용 반환
        return toResponse(schedule);
    }

    // 일정 삭제
    @Transactional
    public void delete(@PathVariable Long id, SchedulerRequest schedulerRequest) {

        // 일정 찾기
        Scheduler schedule = find(id);

        // 비밀번호 체크
        checkPassword(schedulerRequest, schedule);

        // 해당 일정 삭제
        schedulerRepository.deleteById(id);
    }


    // ==== 헬퍼 메서드 ====

    // 정보 반환
    private SchedulerResponse toResponse(Scheduler scheduler) {
        return new SchedulerResponse(
                scheduler.getId(),
                scheduler.getTitle(),
                scheduler.getContents(),
                scheduler.getWriter(),
                scheduler.getCreatedAt(),
                scheduler.getModifiedAt()
        );
    }

    // 일정 찾기 (null 값이면 NOT_FOUND)
    private Scheduler find(Long id) {
        return schedulerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "일정을 찾을 수 없습니다"));
    }

    // 비밀번호 체크 (값이 다르면 BAD_REQUEST)
    private void checkPassword(SchedulerRequest schedulerRequest, Scheduler schedule) {
        if (!schedulerRequest.getPassword().equals(schedule.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다");
        }
    }
}