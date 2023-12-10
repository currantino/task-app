package com.currantino.taskapp.task.dto;

import com.currantino.taskapp.task.entity.TaskPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateTaskDto(
        @NotBlank(message = "Task name must not be blank.")
        String name,
        @NotBlank(message = "Task description must not be blank")
        String description,
        @NotNull(message = "Task priority must not be null.")
        TaskPriority priority,
        @NotNull(message = "Task creator must not be null.")
        Long creatorId,
        Long assigneeId
) {
}
