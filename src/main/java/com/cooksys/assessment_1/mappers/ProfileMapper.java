package com.cooksys.assessment_1.mappers;

import org.mapstruct.Mapper;

import com.cooksys.assessment_1.dtos.ProfileDto;
import com.cooksys.assessment_1.entities.Profile;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
	
	Profile dtoToEntity(ProfileDto profileDto);
}
