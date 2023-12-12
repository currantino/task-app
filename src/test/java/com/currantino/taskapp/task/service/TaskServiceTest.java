package com.currantino.taskapp.task.service;

import com.currantino.taskapp.annotation.EnableTestcontainers;
import com.currantino.taskapp.auth.service.AuthService;
import com.currantino.taskapp.exception.AccessDeniedException;
import com.currantino.taskapp.task.controller.TaskFullDto;
import com.currantino.taskapp.task.dto.CreateTaskDto;
import com.currantino.taskapp.task.dto.TaskFilterDto;
import com.currantino.taskapp.task.dto.UpdateTaskDto;
import com.currantino.taskapp.task.entity.Task;
import com.currantino.taskapp.task.entity.TaskPriority;
import com.currantino.taskapp.task.entity.TaskStatus;
import com.currantino.taskapp.user.entity.Role;
import com.currantino.taskapp.user.entity.User;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@EnableTestcontainers
@AutoConfigureTestEntityManager
class TaskServiceTest {

    @Autowired
    private TestEntityManager em;
    @Autowired
    private TaskService taskService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @MockBean
    private AuthService authService;
    private final Pageable defaultPageable = PageRequest.of(0, 10);
    private User creator;
    private User assignee;
    private Task task;


    @BeforeEach
    void init() {
        creator = em.persistAndFlush(
                User.builder()
                        .email("creator@email.com")
                        .password(passwordEncoder.encode("password"))
                        .role(Role.USER)
                        .build()
        );
        assignee = em.persistAndFlush(
                User.builder()
                        .email("assignee@email.com")
                        .password(passwordEncoder.encode("password"))
                        .role(Role.USER)
                        .build()
        );
        task = Task.builder()
                .name("Task")
                .description("Description of task")
                .priority(TaskPriority.LOW)
                .status(TaskStatus.WAITING)
                .creator(creator)
                .assignee(assignee)
                .build();
        when(authService.getLoggedInUserId()).thenReturn(creator.getId());
    }

    @AfterEach
    void tearDown() {
        em.remove(creator);
        em.remove(assignee);
    }

    @Test
    @Transactional
    void getTasksFiltered() {
        Task task2 = Task.builder()
                .name("Name that contains search pattern")
                .description("Description that contains search pattern")
                .status(TaskStatus.DONE)
                .priority(TaskPriority.HIGH)
                .assignee(assignee)
                .creator(creator)
                .build();
        em.persistAndFlush(task);
        em.persistAndFlush(task2);

        TaskFilterDto filter = TaskFilterDto.builder()
                .status(TaskStatus.DONE)
                .priority(TaskPriority.HIGH)
                .name("pattern")
                .build();
        List<TaskFullDto> actual = taskService.getTasksFiltered(filter, defaultPageable)
                .getContent();
        assertAll(
                () -> assertEquals(1, actual.size()),
                () -> assertEquals(task2.getAssignee().getId(), actual.get(0).assignee().id()),
                () -> assertEquals(task2.getCreator().getId(), actual.get(0).creator().id()),
                () -> assertEquals(task2.getStatus(), actual.get(0).status()),
                () -> assertEquals(task2.getPriority(), actual.get(0).priority()),
                () -> assertEquals(task2.getName(), actual.get(0).name()),
                () -> assertEquals(task2.getDescription(), actual.get(0).description())
        );
    }

    @Test
    @Transactional
    void addTask() {
        CreateTaskDto expected = CreateTaskDto.builder()
                .name("Task 1")
                .description("Description of task 1")
                .creatorId(creator.getId())
                .assigneeId(assignee.getId())
                .priority(TaskPriority.LOW)
                .status(TaskStatus.WAITING)
                .build();
        TaskFullDto actual = taskService.addTask(expected);
        assertAll(
                () -> assertEquals(expected.name(), actual.name()),
                () -> assertEquals(expected.description(), actual.description()),
                () -> assertEquals(expected.status(), actual.status()),
                () -> assertEquals(expected.priority(), actual.priority()),
                () -> assertEquals(expected.assigneeId(), actual.assignee().id()),
                () -> assertEquals(expected.creatorId(), actual.creator().id())
        );
    }

    @Test
    @Transactional
    void getTasksByCreator() {
        em.persistAndFlush(task);
        List<TaskFullDto> tasks = taskService.getTasksByCreator(creator.getId(), defaultPageable)
                .getContent();
        assertAll(
                () -> assertEquals(1, tasks.size()),
                () -> assertEquals(task.getName(), tasks.get(0).name()),
                () -> assertEquals(task.getDescription(), tasks.get(0).description()),
                () -> assertEquals(task.getAssignee().getId(), tasks.get(0).assignee().id()),
                () -> assertEquals(task.getCreator().getId(), tasks.get(0).creator().id())
        );
    }

    @Test
    @Transactional
    void shouldReturnEmptyTasksWhenGetTasksByCreatorWithAssigneeId() {
        em.persistAndFlush(task);
        List<TaskFullDto> tasks = taskService.getTasksByCreator(assignee.getId(), defaultPageable)
                .getContent();
        assertAll(
                () -> assertTrue(tasks.isEmpty())
        );
    }

    @Test
    @Transactional
    void getTasksByAssignee() {
        em.persistAndFlush(task);
        List<TaskFullDto> tasks = taskService.getTasksByAssignee(assignee.getId(), defaultPageable)
                .getContent();
        assertAll(
                () -> assertEquals(1, tasks.size()),
                () -> assertEquals(task.getName(), tasks.get(0).name()),
                () -> assertEquals(task.getDescription(), tasks.get(0).description()),
                () -> assertEquals(task.getAssignee().getId(), tasks.get(0).assignee().id()),
                () -> assertEquals(task.getCreator().getId(), tasks.get(0).creator().id())
        );
    }

    @Test
    @Transactional
    void deleteTaskById() {
        em.persistAndFlush(task);
        taskService.deleteTaskById(task.getId());
        Task deleted = em.find(Task.class, task.getId());
        assertNull(deleted);
    }

    @Test
    @Transactional
    void updateTaskById() {
        em.persistAndFlush(task);
        UpdateTaskDto updateTaskDto = UpdateTaskDto.builder()
                .name("Updated name")
                .description("Updated description")
                .status(TaskStatus.DONE)
                .priority(TaskPriority.HIGH)
                .build();
        taskService.updateTaskById(task.getId(), updateTaskDto);
        Task updated = em.find(Task.class, task.getId());
        assertAll(
                () -> assertNotNull(updated),
                () -> assertEquals(updateTaskDto.name(), updated.getName()),
                () -> assertEquals(updateTaskDto.description(), updated.getDescription()),
                () -> assertEquals(task.getAssignee().getId(), updated.getAssignee().getId()),
                () -> assertEquals(updateTaskDto.priority(), updated.getPriority()),
                () -> assertEquals(updateTaskDto.status(), updated.getStatus()),
                () -> assertEquals(task.getCreator().getId(), updated.getCreator().getId())
        );
    }

    @Test
    @Transactional
    void shouldThrowExceptionWhenUpdateNotByCreator() {
        when(authService.getLoggedInUserId()).thenReturn(2L);
        em.persistAndFlush(task);
        UpdateTaskDto updateTaskDto = UpdateTaskDto.builder()
                .name("Updated name")
                .description("Updated description")
                .status(TaskStatus.DONE)
                .priority(TaskPriority.HIGH)
                .build();
        assertThrows(
                AccessDeniedException.class,
                () -> taskService.updateTaskById(task.getId(), updateTaskDto)
        );
        Task updated = em.find(Task.class, task.getId());
        assertAll(
                () -> assertNotNull(updated),
                () -> assertEquals(task.getName(), updated.getName()),
                () -> assertEquals(task.getDescription(), updated.getDescription()),
                () -> assertEquals(task.getStatus(), updated.getStatus()),
                () -> assertEquals(task.getCreator().getId(), updated.getCreator().getId()),
                () -> assertEquals(task.getAssignee().getId(), updated.getAssignee().getId())
        );
    }

    @Test
    @Transactional
    void updateTaskStatusById() {
        em.persistAndFlush(task);
        TaskStatus newStatus = TaskStatus.IN_PROGRESS;
        taskService.updateTaskStatusById(task.getId(), newStatus);
        Task updated = em.find(Task.class, task.getId());
        assertAll(
                () -> assertNotNull(updated),
                () -> assertEquals(task.getName(), updated.getName()),
                () -> assertEquals(task.getDescription(), updated.getDescription()),
                () -> assertEquals(newStatus, updated.getStatus()),
                () -> assertEquals(task.getCreator().getId(), updated.getCreator().getId()),
                () -> assertEquals(task.getAssignee().getId(), updated.getAssignee().getId())
        );
    }

    @Test
    @Transactional
    void shouldThrowExceptionWhenUpdateTaskStatusNotByCreatorOrAssignee() {
        when(authService.getLoggedInUserId()).thenReturn(99L);
        em.persistAndFlush(task);
        TaskStatus newStatus = TaskStatus.IN_PROGRESS;
        assertThrows(
                AccessDeniedException.class,
                () -> taskService.updateTaskStatusById(task.getId(), newStatus)
        );
        Task notUpdated = em.find(Task.class, task.getId());
        assertAll(
                () -> assertNotNull(notUpdated),
                () -> assertEquals(task.getName(), notUpdated.getName()),
                () -> assertEquals(task.getDescription(), notUpdated.getDescription()),
                () -> assertNotEquals(newStatus, notUpdated.getStatus()),
                () -> assertEquals(task.getCreator().getId(), notUpdated.getCreator().getId()),
                () -> assertEquals(task.getAssignee().getId(), notUpdated.getAssignee().getId())
        );
    }

    @Test
    @Transactional
    void getTaskById() {
        em.persistAndFlush(task);
        TaskFullDto actual = taskService.getTaskById(task.getId());
        assertAll(
                () -> assertEquals(task.getId(), actual.id()),
                () -> assertEquals(task.getName(), actual.name()),
                () -> assertEquals(task.getDescription(), actual.description()),
                () -> assertEquals(task.getPriority(), actual.priority()),
                () -> assertEquals(task.getCreator().getId(), actual.creator().id()),
                () -> assertEquals(task.getAssignee().getId(), actual.assignee().id())
        );

    }
}