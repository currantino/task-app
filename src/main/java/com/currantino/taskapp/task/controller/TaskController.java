package com.currantino.taskapp.task.controller;

import com.currantino.taskapp.task.mapper.TaskMapper;
import com.currantino.taskapp.task.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/task")
public class TaskController {
    private final TaskService taskService;
    private final TaskMapper taskMapper;

    @Autowired
    public TaskController(TaskService taskService, TaskMapper taskMapper) {
        this.taskService = taskService;
        this.taskMapper = taskMapper;
    }

    @GetMapping("/creator/{creatorId}")
    public ResponseEntity<Page<TaskFullDto>> getTasksByCreator(
            @PathVariable Long creatorId,
            @RequestParam(name = "size",
                    defaultValue = "10")
            Integer pageSize,
            @RequestParam(name = "from", defaultValue = "0")
            Integer pageFrom
    ) {
        Pageable pageable = PageRequest.of(pageSize, pageFrom);

        Page<TaskFullDto> tasksByCreator = taskService.getTasksByCreator(creatorId, pageable);
        return ResponseEntity.ok(tasksByCreator);
    }
}
