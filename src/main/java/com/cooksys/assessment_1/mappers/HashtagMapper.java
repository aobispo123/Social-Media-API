package com.cooksys.assessment_1.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.cooksys.assessment_1.dtos.HashtagRequestDto;
import com.cooksys.assessment_1.dtos.HashtagResponseDto;
import com.cooksys.assessment_1.entities.Hashtag;

@Mapper(componentModel = "spring")
public interface HashtagMapper {

	Hashtag DtoToEntity(HashtagRequestDto hashtagRequestDto);

	HashtagResponseDto entityToDto(Hashtag entity);

	List<HashtagResponseDto> entitiesToDtos(List<Hashtag> hashtags);

}
