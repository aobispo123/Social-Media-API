package com.cooksys.assessment_1.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.cooksys.assessment_1.dtos.UserRequestDto;
import com.cooksys.assessment_1.dtos.UserResponseDto;
import com.cooksys.assessment_1.entities.User;

@Mapper(componentModel = "spring", uses = {ProfileMapper.class, CredentialsMapper.class})
public interface UserMapper {
	
	User requestDtoToEntity(UserRequestDto userRequestDto);
	
	@Mapping(source = "credentials.username", target = "username")
	UserResponseDto entityToDto(User user);
	
	List<UserResponseDto> entitiesToDtos(List<User> users);
}
