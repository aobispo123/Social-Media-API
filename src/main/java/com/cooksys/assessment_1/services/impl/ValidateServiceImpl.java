package com.cooksys.assessment_1.services.impl;

import java.util.List;

import org.apache.catalina.User;
import org.springframework.stereotype.Service;

import com.cooksys.assessment_1.entities.Hashtag;
import com.cooksys.assessment_1.mappers.HashtagMapper;
import com.cooksys.assessment_1.repositories.HashtagRepository;
import com.cooksys.assessment_1.repositories.UserRepository;
import com.cooksys.assessment_1.services.ValidateService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ValidateServiceImpl implements ValidateService {
	
	private final HashtagMapper hashtagMapper;
	private final HashtagRepository hashtagRepository;
	private final UserRepository userRepository;

	// COMPLETE
	// GET validate/tag/exists/{label}
	// Checks whether or not a given hashtag exists.
	// Response: 'boolean'
	@Override
	public boolean checkHashtag(String label) {
		List<Hashtag> hashtags =  hashtagRepository.findAll();
//		String checkTagString = "#" + label;
		String checkTagString = label;
		for(Hashtag hashtag: hashtags) {
			if(hashtag.getLabel().equals(checkTagString)) {
				return true;
			}
		}
		return false;
	}
	
	// 	COMPLETE
	//	GET validate/username/exists/@{username}
	//	Checks whether or not a given username exists.
	//	Response:'boolean'
	@Override
	public boolean checkUsernameExists(String username) {
		List<com.cooksys.assessment_1.entities.User> users = userRepository.findAll();
		for(com.cooksys.assessment_1.entities.User user: users) {
			if(user.getCredentials().getUsername().equals(username)) {
				return true;
			}
		}
		return false;
	}

	//	COMPLETE
	//	GET validate/username/available/@{username}
	//	Checks whether or not a given username is available.
	//	Response: 'boolean'	
	@Override
	public boolean checkUsernameAvailable(String username) {
		List<com.cooksys.assessment_1.entities.User> users = userRepository.findAll();
		for(com.cooksys.assessment_1.entities.User user: users) {
			if(user.getCredentials().getUsername().equals(username)) {
				return false;
			}			
		}
		return true;
	}

}
