package com.cooksys.assessment_1.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.assessment_1.dtos.CredentialsDto;
import com.cooksys.assessment_1.dtos.TweetResponseDto;
import com.cooksys.assessment_1.dtos.UserRequestDto;
import com.cooksys.assessment_1.dtos.UserResponseDto;
import com.cooksys.assessment_1.services.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

	private final UserService userService;

	@GetMapping
	public List<UserResponseDto> getAllUsers() {
		return userService.getAllUsers();
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public UserResponseDto createUser(@RequestBody UserRequestDto userRequestDto) {
		return userService.createUser(userRequestDto);
	}

	@GetMapping("/@{username}")
	public UserResponseDto getUser(@PathVariable String username) {
		return userService.getUser(username);
	}

	@PatchMapping("/@{username}")
	public UserResponseDto updateUser(@PathVariable String username, @RequestBody UserRequestDto userRequestDto) {
		return userService.updateUser(username, userRequestDto);
	}

	@DeleteMapping("/@{username}")
	public UserResponseDto deleteUser(@PathVariable String username, @RequestBody CredentialsDto credentialsDto) {
		return userService.deleteUser(username, credentialsDto);
	}

	@PostMapping("/@{username}/follow")
	public void followUser(@PathVariable String username, @RequestBody CredentialsDto credentialsDto) {
		userService.followUser(username, credentialsDto);
	}

	@PostMapping("/@{username}/unfollow")
	public void unfollowUser(@PathVariable String username, @RequestBody CredentialsDto credentialsDto) {
		userService.unfollowUser(username, credentialsDto);
	}

	@GetMapping("/@{username}/feed")
	public List<TweetResponseDto> getUserFeed(@PathVariable String username) {
		return userService.getUserFeed(username);
	}

	@GetMapping("/@{username}/tweets")
	public List<TweetResponseDto> getAllUserTweets(@PathVariable String username) {
		return userService.getAllUserTweets(username);
	}
	
	@GetMapping("/@{username}/mentions")
	public List<TweetResponseDto> getAllUserMentions(@PathVariable String username) {
		return userService.getAllUserMentions(username);
	}


	@GetMapping("/@{username}/followers")
	public List<UserResponseDto> getAllUserFollowers(@PathVariable String username) {
		return userService.getAllUserFollowers(username);
	}

	@GetMapping("/@{username}/following")
	public List<UserResponseDto> getAllUsersFollowed(@PathVariable String username) {
		return userService.getAllUsersFollowed(username);
	}
	
	
}
