package com.currantino.taskapp.comment.service;

import com.currantino.taskapp.auth.service.AuthService;
import com.currantino.taskapp.comment.dto.CommentDto;
import com.currantino.taskapp.comment.dto.CreateCommentDto;
import com.currantino.taskapp.comment.entity.Comment;
import com.currantino.taskapp.comment.mapper.CommentMapper;
import com.currantino.taskapp.comment.repository.CommentRepository;
import com.currantino.taskapp.exception.AccessDeniedException;
import com.currantino.taskapp.exception.CommentNotFoundException;
import com.currantino.taskapp.exception.TaskNotFoundException;
import com.currantino.taskapp.task.entity.Task;
import com.currantino.taskapp.task.repository.TaskRepository;
import com.currantino.taskapp.user.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final AuthService authService;
    private final CommentMapper commentMapper;
    private final TaskRepository taskRepository;

    public CommentService(CommentRepository commentRepository, AuthService authService, CommentMapper commentMapper, TaskRepository taskRepository) {
        this.commentRepository = commentRepository;
        this.authService = authService;
        this.commentMapper = commentMapper;
        this.taskRepository = taskRepository;
    }

    public CommentDto addComment(CreateCommentDto commentDto, Long taskId) {
        Long realRequesterId = authService.getLoggedInUserId();
        if (!commentDto.authorId().equals(realRequesterId)) {
            throw new AccessDeniedException("Invalid author id: " + commentDto.authorId());
        }
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Could not find task with id: " + taskId));
        Comment comment = commentMapper.toEntity(commentDto);
        Comment saved = commentRepository.save(comment);
        return commentMapper.toDto(saved);
    }

    public Page<CommentDto> getCommentsByTaskId(Long taskId, Pageable pageable) {
        return commentRepository.findByTaskId(taskId, pageable);
    }

    public void deleteCommentById(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Could not find comment with id: " + commentId));
        Long realRequesterId = authService.getLoggedInUserId();
        Role realRequesterRole = authService.getLoggedInUserRole();
        if (!comment.getAuthor().getId().equals(realRequesterId) && realRequesterRole.equals(Role.ADMIN)) {
            throw new AccessDeniedException("You can not delete comments of other users.");
        }
        commentRepository.deleteById(commentId);
    }

}
