package com.currantino.taskapp.comment.dto;

import com.currantino.taskapp.user.dto.UserFullDto;

public record CommentDto(Long id, String text, UserFullDto author, Long taskId) {
}
