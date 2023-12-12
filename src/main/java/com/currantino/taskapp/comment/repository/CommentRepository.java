package com.currantino.taskapp.comment.repository;

import com.currantino.taskapp.comment.dto.CommentDto;
import com.currantino.taskapp.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("""
            SELECT c
            FROM Comment c
            WHERE c.task.id = :taskId
            """)
    Page<CommentDto> findByTaskId(Long taskId, Pageable pageable);
}