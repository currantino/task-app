package com.currantino.taskapp.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCommentDto(
        Long authorId,
        @NotBlank(message = "Comment text must not be blank.")
        @Size(max = 1023,
                message = "Comment text must be less than 1023 long.")
        String text
) {
}
