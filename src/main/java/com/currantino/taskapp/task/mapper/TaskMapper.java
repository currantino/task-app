package com.currantino.taskapp.task.mapper;

import com.currantino.taskapp.config.CentralMapstructConfig;
import com.currantino.taskapp.task.controller.TaskFullDto;
import com.currantino.taskapp.task.dto.CreateTaskDto;
import com.currantino.taskapp.task.dto.UpdateTaskDto;
import com.currantino.taskapp.task.entity.Task;
import com.currantino.taskapp.user.entity.User;
import com.currantino.taskapp.user.mapper.UserMapper;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(config = CentralMapstructConfig.class, uses = {UserMapper.class})
public interface TaskMapper {

    TaskFullDto toFullDto(Task task);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "assignee.id", source = "assigneeId")
    Task partialUpdate(UpdateTaskDto taskFullDto, @MappingTarget Task task);

    @Mapping(target = "assignee", source = "assigneeId", qualifiedByName = "mapAssignee")
    @Mapping(target = "creator.id", source = "creatorId")
    Task toEntity(CreateTaskDto createTaskDto);

    @Named("mapAssignee")
    default User mapAssigneeId(Long id) {
        if (id == null) {
            return null;
        }
        return User.builder()
                .id(id)
                .build();
    }

}
