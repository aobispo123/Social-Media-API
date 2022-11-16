package com.cooksys.assessment_1.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.assessment_1.dtos.HashtagResponseDto;
import com.cooksys.assessment_1.dtos.TweetResponseDto;
import com.cooksys.assessment_1.services.HashtagService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tags")
public class HashtagController {
	
	private final HashtagService hashtagService;
	
	@GetMapping
	public List<HashtagResponseDto> getAllHashtags(){
		return hashtagService.getAllHashtags();
	}
	
	@GetMapping("/{label}")
	public List<TweetResponseDto> getTweetsByHashtag(@PathVariable String label){
		return hashtagService.getTweetsByHashtag(label);
	}
		

}
