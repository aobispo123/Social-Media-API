package com.cooksys.assessment_1.services;

import java.util.List;

import com.cooksys.assessment_1.dtos.CredentialsDto;
import com.cooksys.assessment_1.dtos.TweetResponseDto;
import com.cooksys.assessment_1.dtos.UserRequestDto;
import com.cooksys.assessment_1.dtos.UserResponseDto;

public interface UserService {

	List<UserResponseDto> getAllUsers();

	UserResponseDto createUser(UserRequestDto userRequestDto);
	
	UserResponseDto getUser(String username);
	
	UserResponseDto updateUser(String username, UserRequestDto userRequestDto);

	UserResponseDto deleteUser(String username, CredentialsDto credentialsDto);

	void followUser(String username, CredentialsDto credentialsDto);

	void unfollowUser(String username, CredentialsDto credentialsDto);
	
	List<TweetResponseDto> getUserFeed(String username);

	List<TweetResponseDto> getAllUserTweets(String username);

	List<TweetResponseDto> getAllUserMentions(String username);

	List<UserResponseDto> getAllUserFollowers(String username);

	List<UserResponseDto> getAllUsersFollowed(String username);

	
	
}
