package com.cooksys.assessment_1.mappers;

import org.mapstruct.Mapper;

import com.cooksys.assessment_1.dtos.CredentialsDto;
import com.cooksys.assessment_1.entities.Credentials;

@Mapper(componentModel = "spring")
public interface CredentialsMapper {
	
	Credentials dtoToEntity(CredentialsDto credentialsDto);
	
	CredentialsDto entityToDto(Credentials credentials);
}
