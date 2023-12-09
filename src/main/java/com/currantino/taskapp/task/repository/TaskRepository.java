package com.currantino.taskapp.task.repository;

import com.currantino.taskapp.task.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query("""
            SELECT t
            FROM Task t
            WHERE t.creator.id = :creatorId
            ORDER BY t.priority DESC
            """)
    Page<Task> findAllByCreator(Long creatorId, Pageable pageable);

    //TODO: ADD FILTERING
    @Query("""
            SELECT t
            FROM Task t
            WHERE t.assignee.id = :assigneeId
            ORDER BY t.priority DESC
            """)
    Page<Task> findAllByAssignee(Long assigneeId, Pageable pageable);
}