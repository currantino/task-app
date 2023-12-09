package com.currantino.taskapp.task.entity;

import com.currantino.taskapp.comment.entity.Comment;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

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
    @JoinColumn(name = "task_creator_id",
            nullable = false)
    private User creator;

    @ManyToOne
    @JoinColumn(name = "task_assignee_id")
    private User assignee;

    @OneToMany
    @JoinColumn(name = "comment_id")
    private List<Comment> comments;

}
