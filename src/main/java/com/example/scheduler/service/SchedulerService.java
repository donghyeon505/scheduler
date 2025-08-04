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
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SchedulerService {

    private final SchedulerRepository schedulerRepository;
    private final CommentRepository commentRepository;

    // 일정 등록
    @Transactional
    public SchedulerResponse save(SchedulerRequest schedulerRequest) {

        // 문자열 길이와 Null 값 체크
        checkTitleContents(schedulerRequest);
        checkWriter(schedulerRequest);
        checkPassword(schedulerRequest);

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
      public List<SchedulerResponse> findAll(String writer) {

          // 리스트 찾기
          List<Scheduler> schedulers = schedulerRepository.findAll();

          // 내림차순 정렬
          schedulers.sort(Comparator.comparing(Scheduler::getModifiedAt).reversed());

          // 리스트에 값넣기
          List<SchedulerResponse> schedulerResponses;
          schedulerResponses = toResponseList(schedulers, writer);

          // 반환
          return schedulerResponses;
      }

    // 선택 일정 조회
    @Transactional (readOnly = true)
    public SchedulerCommentResponse findById(Long id) {

        // 일정 찾기
        Scheduler schedule = find(id);

        // 일정 Response로 변환
        SchedulerResponse schedules = toResponse(schedule);

        // 댓글 조회 하기 ( schedulerId가 Id 값과 같을 때 )
        List<Comment> comments = commentRepository.findAllBySchedulerId(id);

        // 응답 객체 리스트 초기화
        List<CommentResponse> commentResponses = new ArrayList<>();

        // 받은 정보를 Response 객체로 변환해서 저장 (해당 일정 아이디 값만)
        for (Comment comment : comments) {
                commentResponses.add(toResponseComment(comment));
        }

        // 조회된 일정과 댓글 반환
        return new SchedulerCommentResponse(schedules, commentResponses);
    }

    // 일정 수정
    @Transactional
    public SchedulerResponse updateSchedule(Long id, SchedulerRequest schedulerRequest) {

        // 문자열 길이와 Null 값 체크
        checkTitleContents(schedulerRequest);
        checkPassword(schedulerRequest);

        // 일정 찾기
        Scheduler schedule = find(id);

        // 비밀번호 체크
        checkPassword(schedulerRequest, schedule);

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
    public void delete(Long id, SchedulerRequest schedulerRequest) {

        // 비밀번호 Null 값 체크
        checkPassword(schedulerRequest);

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

    // 댓글 정보 반환
    private CommentResponse toResponseComment(Comment comment) {
        return new CommentResponse(
                comment.getComment(),
                comment.getWriter(),
                comment.getCreatedAt(),
                comment.getModifiedAt(),
                comment.getSchedulerId()
        );
    }

    // 일정 조회 리스트 반환 메서드
    private List<SchedulerResponse> toResponseList(List<Scheduler> schedulers, String writer) {
        List<SchedulerResponse> schedulerResponses = new ArrayList<>();

        if (writer == null) {
            for (Scheduler scheduler : schedulers) {
                schedulerResponses.add(toResponse(scheduler));
            }
        } else {
            for (Scheduler scheduler : schedulers) {
                if  (scheduler.getWriter().equals(writer)) {
                    schedulerResponses.add(toResponse(scheduler));
                }
            }
        }

        return schedulerResponses;
    }

    // 일정 찾기 (null 값이면 NOT_FOUND)
    private Scheduler find(Long id) {
        return schedulerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "일정을 찾을 수 없습니다"));
    }

    // 비밀번호 체크 (값이 다르면 BAD_REQUEST)
    private void checkPassword(SchedulerRequest schedulerRequest, Scheduler schedule) {
        if (!schedulerRequest.getPassword().equals(schedule.getPassword())) {
            throwNewBadRequest("비밀번호가 일치하지 않습니다");
        }
    }

    // 제목과 내용 체크
    private void checkTitleContents(SchedulerRequest schedulerRequest) {
        if (schedulerRequest.getTitle() == null || schedulerRequest.getTitle().isBlank()) {
            throwNewBadRequest("제목은 필수입니다.");
        }

        if (schedulerRequest.getTitle().length() > 30) {
            throwNewBadRequest("제목의 길이는 30자 이내여야 합니다.");
        }

        if (schedulerRequest.getContents() == null || schedulerRequest.getContents().isBlank()) {
            throwNewBadRequest("내용은 필수입니다.");
        }

        if (schedulerRequest.getContents().length() > 200) {
            throwNewBadRequest("내용의 길이는 200자 이내여야 합니다.");
        }

    }

    // 작성자 체크
    private void checkWriter(SchedulerRequest schedulerRequest) {
        if (schedulerRequest.getWriter() == null || schedulerRequest.getWriter().isBlank()) {
            throwNewBadRequest("작성자 이름은 필수입니다.");
        }
    }

    // 비밀번호 체크
    private void checkPassword(SchedulerRequest schedulerRequest) {
        if (schedulerRequest.getPassword() == null || schedulerRequest.getPassword().isBlank()) {
            throwNewBadRequest("비밀번호는 필수입니다.");
        }
    }

    // BAD_REQUEST 오류 발생
    private void throwNewBadRequest(String text) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, text);
    }
}