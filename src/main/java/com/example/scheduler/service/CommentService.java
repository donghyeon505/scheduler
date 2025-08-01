package com.example.scheduler.service;

import com.example.scheduler.dto.CommentRequest;
import com.example.scheduler.dto.CommentResponse;
import com.example.scheduler.entity.Comment;
import com.example.scheduler.repository.CommentRepository;
import com.example.scheduler.repository.SchedulerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final SchedulerRepository schedulerRepository;

    // 댓글 생성 기능
    @Transactional
    public CommentResponse addComment(@PathVariable Long id, @RequestBody CommentRequest commentRequest) {

        // 문자열 길이와 Null 값 체크
        checkComment(commentRequest);

        // 일정 찾기
        schedulerRepository.findById(id).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "일정을 찾을 수 없습니다."));

        // 일정 댓글 카운팅 (10개 까지만 허용)
        Long countComment = commentRepository.countBySchedulerId(id);
        if (countComment >= 10) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "댓글은 10개까지만 작성가능합니다.");
        }

        // comment 객체 생성 및 정보 저장
        Comment comment = new Comment(
                commentRequest.getComment(),
                commentRequest.getWriter(),
                commentRequest.getPassword()
        );
        // SchedulerId 직접 값 넣어주기 (해당 일정의 id)
        comment.setSchedulerId(id);

        // DB에 저장
        Comment savedComment = commentRepository.save(comment);

        // 저장된 댓글 정보 반환 (비밀번호 제외, 해당 일정의 id만 반환)
        return toResponseComment(savedComment);
    }


    // ==== 헬퍼 메서드 ====

    // Response 변환
    private CommentResponse toResponseComment(Comment comment) {
        return new CommentResponse(
                comment.getComment(),
                comment.getWriter(),
                comment.getCreatedAt(),
                comment.getModifiedAt(),
                comment.getSchedulerId()
        );
    }

    // 댓글, 작성자, 비밀번호 체크
    private void checkComment(CommentRequest commentRequest) {
        if (commentRequest.getComment() == null || commentRequest.getComment().isBlank()) {
            throwNewBadRequest("댓글 내용은 필수입니다.");
        }

        if (commentRequest.getComment().length() > 100) {
            throwNewBadRequest("댓글의 길이는 100자 이내입니다");
        }

        if (commentRequest.getWriter() == null || commentRequest.getWriter().isBlank()) {
            throwNewBadRequest("작성자 이름은 필수입니다.");
        }

        if (commentRequest.getPassword() == null || commentRequest.getPassword().isBlank()) {
            throwNewBadRequest("비밀번호는 필수입니다.");
        }
    }

    // BAD_REQUEST 오류 발생
    private void throwNewBadRequest(String text) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, text);
    }
}
