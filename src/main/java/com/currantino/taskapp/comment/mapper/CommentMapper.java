package com.currantino.taskapp.comment.mapper;

import com.currantino.taskapp.comment.dto.CommentDto;
import com.currantino.taskapp.comment.dto.CreateCommentDto;
import com.currantino.taskapp.comment.entity.Comment;
import com.currantino.taskapp.config.CentralMapstructConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = CentralMapstructConfig.class)
public interface CommentMapper {
    @Mapping(target = "author.id", source = "authorId")
    Comment toEntity(CreateCommentDto dto);

    @Mapping(target = "taskId", source = "task.id")
    CommentDto toDto(Comment comment);
}
