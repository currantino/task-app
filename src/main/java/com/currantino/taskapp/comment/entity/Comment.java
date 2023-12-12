package com.currantino.taskapp.comment.entity;

import com.currantino.taskapp.task.entity.Task;
import com.currantino.taskapp.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id", nullable = false)
    private Long id;

    @Column(name = "comment_text",
            nullable = false,
            length = 1023)
    private String text;

    @ManyToOne
    @JoinColumn(name = "comment_author_id",
            nullable = false,
            updatable = false)
    private User author;

    @ManyToOne
    @JoinColumn(name = "comment_task_id",
            nullable = false,
            updatable = false)
    private Task task;

}
