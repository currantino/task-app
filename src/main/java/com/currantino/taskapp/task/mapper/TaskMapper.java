package com.currantino.taskapp.task.mapper;

import com.currantino.taskapp.config.CentralMapstructConfig;
import com.currantino.taskapp.task.controller.TaskFullDto;
import com.currantino.taskapp.task.entity.Task;
import com.currantino.taskapp.user.mapper.UserMapper;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(config = CentralMapstructConfig.class, uses = {UserMapper.class, UserMapper.class})
public interface TaskMapper {

    Task toEntity(TaskFullDto taskFullDto);

    TaskFullDto toFullDto(Task task);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Task partialUpdate(TaskFullDto taskFullDto, @MappingTarget Task task);
}
