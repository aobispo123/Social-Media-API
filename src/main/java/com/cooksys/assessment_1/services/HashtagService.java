package com.cooksys.assessment_1.services;

import java.util.List;

import com.cooksys.assessment_1.dtos.HashtagResponseDto;
import com.cooksys.assessment_1.dtos.TweetResponseDto;

public interface HashtagService {
	
	List<HashtagResponseDto> getAllHashtags();

	List<TweetResponseDto> getTweetsByHashtag(String label);

}
