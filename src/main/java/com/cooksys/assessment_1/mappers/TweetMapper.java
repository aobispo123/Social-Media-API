package com.cooksys.assessment_1.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.cooksys.assessment_1.dtos.TweetRequestDto;
import com.cooksys.assessment_1.dtos.TweetResponseDto;
import com.cooksys.assessment_1.entities.Tweet;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface TweetMapper {
    
    Tweet DtoToEntity(TweetRequestDto tweetRequestDto);

    TweetResponseDto entityToDto(Tweet entity);

    List<TweetResponseDto> entitiesToDtos(List<Tweet> tweets);
    
}
