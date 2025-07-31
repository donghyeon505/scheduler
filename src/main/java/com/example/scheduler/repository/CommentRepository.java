package com.example.scheduler.repository;

import com.example.scheduler.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Long countBySchedulerId(Long schedulerId);
}
