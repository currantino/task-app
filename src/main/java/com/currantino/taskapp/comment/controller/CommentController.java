package com.currantino.taskapp.comment.controller;

import com.currantino.taskapp.comment.dto.CommentDto;
import com.currantino.taskapp.comment.dto.CreateCommentDto;
import com.currantino.taskapp.comment.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/comment")
@SecurityRequirement(name = "jwtAuth")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @Operation(summary = "Add a new comment to task by its id.")
    @PostMapping("/task/{taskId}")
    public ResponseEntity<CommentDto> addCommentByTaskId(@PathVariable
                                                         Long taskId,
                                                         @Valid
                                                         @RequestBody
                                                         CreateCommentDto commentDto) {
        CommentDto comment = commentService.addComment(commentDto, taskId);
        return ResponseEntity.ok(comment);
    }

    @Operation(summary = "Get comments of a task by its id with pagination.")
    @GetMapping("/task/{taskId}")
    public ResponseEntity<Page<CommentDto>> getCommentsByTaskId(@PathVariable
                                                                Long taskId,
                                                                @RequestParam(name = "page", defaultValue = "0")
                                                                Integer pageNumber,
                                                                @RequestParam(name = "size", defaultValue = "10")
                                                                Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<CommentDto> comments = commentService.getCommentsByTaskId(taskId, pageable);
        return ResponseEntity.ok(comments);
    }

    @Operation(summary = "Delete comment by its id. Only comment author and admins can delete it.")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteCommentById(@PathVariable
                                                  Long commentId) {
        commentService.deleteCommentById(commentId);
        return ResponseEntity.noContent().build();
    }
}

