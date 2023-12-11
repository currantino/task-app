package com.currantino.taskapp.task.controller;

import com.currantino.taskapp.task.dto.CreateTaskDto;
import com.currantino.taskapp.task.dto.TaskFilterDto;
import com.currantino.taskapp.task.dto.UpdateTaskDto;
import com.currantino.taskapp.task.entity.TaskPriority;
import com.currantino.taskapp.task.entity.TaskStatus;
import com.currantino.taskapp.task.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/task")
public class TaskController {
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping()
    public ResponseEntity<TaskFullDto> addTask(
            @Valid
            @RequestBody
            CreateTaskDto createTaskDto
    ) {
        TaskFullDto task = taskService.addTask(createTaskDto);
        return ResponseEntity.ok(task);
    }

    @GetMapping()
    public Page<TaskFullDto> getTasksFiltered(
            @RequestParam(name = "size",
                    defaultValue = "10")
            Integer pageSize,
            @RequestParam(name = "from", defaultValue = "0")
            Integer pageFrom,
            @RequestParam(required = false)
            String name,
            @RequestParam(required = false)
            String description,
            @RequestParam(required = false)
            TaskStatus status,
            @RequestParam(required = false)
            TaskPriority priority,
            @RequestParam(required = false)
            Long assigneeId,
            @RequestParam(required = false)
            Long creatorId
    ) {
        TaskFilterDto filter = new TaskFilterDto(
                name,
                description,
                status,
                priority,
                assigneeId,
                creatorId
        );
        Pageable pageable = PageRequest.of(pageFrom, pageSize);
        return taskService.getTasksFiltered(filter, pageable);
    }


    @GetMapping("/creator/{creatorId}")
    public ResponseEntity<Page<TaskFullDto>> getTasksByCreator(
            @PathVariable
            Long creatorId,
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

    @GetMapping("/assignee/{assigneeId}")
    public ResponseEntity<Page<TaskFullDto>> getTasksByAssignee(
            @PathVariable
            Long assigneeId,
            @RequestParam(name = "size",
                    defaultValue = "10")
            Integer pageSize,
            @RequestParam(name = "from", defaultValue = "0")
            Integer pageFrom
    ) {
        Pageable pageable = PageRequest.of(pageSize, pageFrom);
        Page<TaskFullDto> tasksByCreator = taskService.getTasksByAssignee(assigneeId, pageable);
        return ResponseEntity.ok(tasksByCreator);
    }

    @PatchMapping("/{taskId}")
    public ResponseEntity<TaskFullDto> updateMappingById(
            @PathVariable
            Long taskId,
            @Valid
            @RequestBody
            UpdateTaskDto updateTaskDto
    ) {
        TaskFullDto task = taskService.updateTaskById(taskId, updateTaskDto);
        return ResponseEntity.ok(task);
    }

    @PatchMapping("/{taskId}/{taskStatus}")
    public ResponseEntity<TaskFullDto> updateTaskStatusById(
            @PathVariable
            Long taskId,
            @PathVariable
            TaskStatus taskStatus
    ) {
        TaskFullDto task = taskService.updateTaskStatusById(taskId, taskStatus);
        return ResponseEntity.ok(task);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTaskById(
            @PathVariable
            Long taskId
    ) {
        taskService.deleteTaskById(taskId);
        return ResponseEntity.noContent().build();
    }


}
