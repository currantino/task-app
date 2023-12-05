package com.currantino.taskapp.task.entity;

import com.currantino.taskapp.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "task")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long id;

    @Column(name = "task_name",
            nullable = false,
            length = 255)
    private String name;
    @Column(name = "task_description",
            nullable = false,
            length = 2047)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "task_status",
            nullable = false)
    private TaskStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "task_priority",
            nullable = false)
    private TaskPriority priority;

    @ManyToOne
    @JoinColumn(name = "creator_id",
            nullable = false)
    private User creator;

    @ManyToOne
    @JoinColumn(name = "assignee_id")
    private User assignee;

}
