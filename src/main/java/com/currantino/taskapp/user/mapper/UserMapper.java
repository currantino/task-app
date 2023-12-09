package com.currantino.taskapp.user.mapper;

import com.currantino.taskapp.config.CentralMapstructConfig;
import com.currantino.taskapp.user.dto.UserCredentialsDto;
import com.currantino.taskapp.user.dto.UserFullDto;
import com.currantino.taskapp.user.entity.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(config = CentralMapstructConfig.class)
public interface UserMapper {
    User toEntity(UserCredentialsDto dto);

    User toEntity(UserFullDto dto);

    UserFullDto toFullDto(User savedUser);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdate(UserFullDto userFullDto, @MappingTarget User user);
}
