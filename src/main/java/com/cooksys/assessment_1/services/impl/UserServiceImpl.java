package com.cooksys.assessment_1.services.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cooksys.assessment_1.dtos.CredentialsDto;
import com.cooksys.assessment_1.dtos.ProfileDto;
import com.cooksys.assessment_1.dtos.TweetResponseDto;
import com.cooksys.assessment_1.dtos.UserRequestDto;
import com.cooksys.assessment_1.dtos.UserResponseDto;
import com.cooksys.assessment_1.entities.Credentials;
import com.cooksys.assessment_1.entities.Profile;
import com.cooksys.assessment_1.entities.Tweet;
import com.cooksys.assessment_1.entities.User;
import com.cooksys.assessment_1.exceptions.BadRequestException;
import com.cooksys.assessment_1.exceptions.NotFoundException;
import com.cooksys.assessment_1.mappers.CredentialsMapper;
import com.cooksys.assessment_1.mappers.ProfileMapper;
import com.cooksys.assessment_1.mappers.TweetMapper;
import com.cooksys.assessment_1.mappers.UserMapper;
import com.cooksys.assessment_1.repositories.TweetRepository;
import com.cooksys.assessment_1.repositories.UserRepository;
import com.cooksys.assessment_1.services.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserMapper userMapper;
	private final UserRepository userRepository;
	private final CredentialsMapper credentialsMapper;
	private final ProfileMapper profileMapper;
	private final TweetMapper tweetMapper;
	private final TweetRepository tweetRepository;

	// Helper methods

	// Checkers when a UserRequestDto is requested
	// Checks Credentials of UserRequestDto whether they are missing required fields
	// or if already exists
	public Credentials checkCredentials(Credentials credentials) {
		List<User> users = userRepository.findAll();
		if (credentials == null) {
			throw new BadRequestException("Missing username and password");
		}
		if (credentials.getPassword() == null || credentials.getUsername() == null) {
			throw new BadRequestException("Missing username or password");
		}
		for (User user : users) {
			if (user.getCredentials().getUsername().equals(credentials.getUsername()) && !user.isDeleted()) {
				throw new BadRequestException("This username already exists");
			}
		}

		return credentials;
	}

	// Checks Profile of UserRequestDto if missing required field
	public Profile checkProfile(Profile profile) {
		if (profile == null) {
			throw new BadRequestException("Need to fill in a profile");
		}
		if (profile.getEmail() == null) {
			throw new BadRequestException("Email is required");
		}
		return profile;
	}

	// Checks if User has been deleted or Does Not Exist then returns correct User
	// if only username/CredentialsDto is requested
	public User getUserByCredentials(Credentials credentials) {
		List<User> users = userRepository.findAll();
		Optional<User> optionalUser;

		for (User user : users) {
			if (user.getCredentials().equals(credentials)) {
				if (user.isDeleted()) {
					throw new BadRequestException("This user has been deleted");
				}
				Long id = user.getId();
				optionalUser = userRepository.findByIdAndDeletedFalse(id);
				return optionalUser.get();
			}
		}
		throw new NotFoundException("Given credentials don't match any user's credentials");
	}

	// Checks if User has been deleted or Does Not Exist then returns correct User
	// if only username/CredentialsDto is requested
	public User getUserByUsername(String username) {
		List<User> users = userRepository.findAll();
		Optional<User> optionalUser;

		for (User user : users) {
			if (user.getCredentials().getUsername().equals(username)) {
				// Checks if user is deleted
				if (user.isDeleted()) {
					throw new BadRequestException("This user has been deleted");
				}
				Long id = user.getId();
				optionalUser = userRepository.findByIdAndDeletedFalse(id);
				return optionalUser.get();
			}
		}
		// At this point user does not exist
		throw new NotFoundException("User with username: " + username + " not found");
	}

	// ENDPOINTS

	@Override
	public List<UserResponseDto> getAllUsers() {
		return userMapper.entitiesToDtos(userRepository.findAllByDeletedFalse());
	}

	@Override
	public UserResponseDto createUser(UserRequestDto userRequestDto) {
		User userToCreate = userMapper.requestDtoToEntity(userRequestDto);
		List<User> users = userRepository.findAll();

		CredentialsDto userCredentialsDto = userRequestDto.getCredentials();
		Credentials userCredentials = credentialsMapper.dtoToEntity(userCredentialsDto);

		checkCredentials(userCredentials);

		for (User user : users) {
			if (user.getCredentials().equals(userCredentials) && user.isDeleted() == true) {
				user.setDeleted(false);
				user.getProfile().setFirstName(userToCreate.getProfile().getFirstName());
				user.getProfile().setLastName(userToCreate.getProfile().getLastName());
				user.getProfile().setEmail(userToCreate.getProfile().getEmail());
				user.getProfile().setPhone(userToCreate.getProfile().getPhone());
				userToCreate = user;
				return userMapper.entityToDto(userRepository.saveAndFlush(userToCreate));
			}
		}

		ProfileDto userProfileDto = userRequestDto.getProfile();
		Profile userProfile = profileMapper.dtoToEntity(userProfileDto);

		checkProfile(userProfile);

		userToCreate.setCredentials(userCredentials);
		userToCreate.setProfile(userProfile);

		return userMapper.entityToDto(userRepository.saveAndFlush(userToCreate));

	}

	@Override
	public UserResponseDto getUser(String username) {
		return userMapper.entityToDto(userRepository.saveAndFlush(getUserByUsername(username)));
	}

	@Override
	public UserResponseDto updateUser(String username, UserRequestDto userRequestDto) {
		User userToUpdate = getUserByUsername(username);
		Credentials credentials = credentialsMapper.dtoToEntity(userRequestDto.getCredentials());

		if (!userToUpdate.getCredentials().equals(credentials)) {
			throw new BadRequestException("Given credentials don't match designated user's credentials");
		}

		Profile newProfile = profileMapper.dtoToEntity(userRequestDto.getProfile());
		if (newProfile == null) {
			throw new BadRequestException("Profile missing");
		}
		if (newProfile.getEmail() == null) {
			newProfile.setEmail(userToUpdate.getProfile().getEmail());
		}
		if (newProfile.getFirstName() == null) {
			newProfile.setFirstName(userToUpdate.getProfile().getFirstName());
		}
		if (newProfile.getLastName() == null) {
			newProfile.setLastName(userToUpdate.getProfile().getLastName());
		}
		if (newProfile.getPhone() == null) {
			newProfile.setPhone(userToUpdate.getProfile().getPhone());
		}

		userToUpdate.setProfile(newProfile);
		return userMapper.entityToDto(userRepository.saveAndFlush(userToUpdate));
	}

	@Override
	public UserResponseDto deleteUser(String username, CredentialsDto credentialsDto) {
		User userToDelete = getUserByUsername(username);
		Credentials credentials = credentialsMapper.dtoToEntity(credentialsDto);

		if (!userToDelete.getCredentials().equals(credentials)) {
			throw new BadRequestException("Given credentials don't match designated user's credentials");
		}

		userToDelete.setDeleted(true);
		return userMapper.entityToDto(userRepository.saveAndFlush(userToDelete));

	}

	@Override
	public void followUser(String username, CredentialsDto credentialsDto) {
		User followedUser = getUserByUsername(username);
		List<User> userFollowers = followedUser.getFollowers();

		Credentials credentials = credentialsMapper.dtoToEntity(credentialsDto);
		User followingUser = getUserByCredentials(credentials);

		for (User user : userFollowers) {
			if (user.equals(followingUser)) {
				throw new BadRequestException("User is already following " + username);
			}
		}

		followedUser.getFollowers().add(followingUser);
		userRepository.saveAndFlush(followedUser);

	}

	@Override
	public void unfollowUser(String username, CredentialsDto credentialsDto) {
		User followedUser = getUserByUsername(username);
		List<User> userFollowers = followedUser.getFollowers();

		Credentials credentials = credentialsMapper.dtoToEntity(credentialsDto);
		User unfollowingUser = getUserByCredentials(credentials);

		if (!userFollowers.contains(unfollowingUser))
			throw new BadRequestException("User was not following: " + username);

		followedUser.getFollowers().remove(unfollowingUser);
		userRepository.saveAndFlush(followedUser);

	}

	// "/@{username}/feed"
	// Retrieves all (non-deleted) tweets authored by the user with the given
	// username, as well as all (non-deleted) tweets authored by users the given
	// user is
	// following. This includes simple tweets, reposts, and replies. The tweets
	// should appear
	// in reverse-chronological order.
	@Override
	public List<TweetResponseDto> getUserFeed(String username) {
		User targetUser = getUserByUsername(username);
		List<User> users = targetUser.getFollowing();
		users.add(targetUser);

		List<Tweet> tweetsList = new ArrayList<>();

		for (User user : users) {
			List<Tweet> userTweets = user.getTweets();
			for (Tweet tweet : userTweets) {
				if (!tweet.isDeleted()) {
					tweetsList.add(tweet);
				}
			}

		}

		List<Tweet> feedList = new ArrayList<>();
		feedList.addAll(tweetsList);

		for (Tweet tweet : tweetsList) {
			List<Tweet> tweetReplyList = tweet.getReplies();
			for (Tweet tweetReply : tweetReplyList) {
				if (!tweetReply.isDeleted()) {
					feedList.add(tweetReply);
				}
			}
			List<Tweet> tweetRepostList = tweet.getReposts();
			for (Tweet tweetRepost : tweetRepostList) {
				if (!tweetRepost.isDeleted()) {
					feedList.add(tweetRepost);
				}
			}

		}

		feedList = new ArrayList<>(new HashSet<>(feedList));
		Collections.sort(feedList, Comparator.comparing(Tweet::getPosted));
		Collections.reverse(feedList);

		return tweetMapper.entitiesToDtos(feedList);
	}

	@Override
	public List<TweetResponseDto> getAllUserTweets(String username) {
		User userWithTweets = getUserByUsername(username);
		List<Tweet> userTweets = userWithTweets.getTweets();
		List<Tweet> newUserTweets = new ArrayList<>();

		for (Tweet tweet : userTweets) {
			if (!tweet.isDeleted()) {
				newUserTweets.add(tweet);
			}
		}

		newUserTweets = new ArrayList<>(new HashSet<>(newUserTweets));
		Collections.sort(newUserTweets, Comparator.comparing(Tweet::getPosted));
		Collections.reverse(newUserTweets);

		return tweetMapper.entitiesToDtos(tweetRepository.saveAllAndFlush(newUserTweets));
	}

	// ("/@{username}/mentions")
	// Retrieves all (non-deleted) tweets in which the user with the given username
	// is mentioned.
	// The tweets should appear in reverse-chronological order. If no active user
	// with that username
	// exists, an error should be sent in lieu of a response.
	// A user is considered "mentioned" by a tweet if the tweet has content and the
	// user's username
	// appears in that content following a @.
	// Response: ['Tweet']
	@Override
	public List<TweetResponseDto> getAllUserMentions(String username) {
		User targetUser = getUserByUsername(username);
		List<Tweet> allTweets = tweetRepository.findAllByDeletedFalse();
		List<Tweet> mentionsTweetList = new ArrayList<>();
		for (Tweet tweet : allTweets) {
			List<User> mentionedUsers = tweet.getMentionedUsers();
			if (mentionedUsers.contains(targetUser)) {
				mentionsTweetList.add(tweet);
			}

		}

		mentionsTweetList = new ArrayList<>(new HashSet<>(mentionsTweetList));
		Collections.sort(mentionsTweetList, Comparator.comparing(Tweet::getPosted));
		Collections.reverse(mentionsTweetList);

		return tweetMapper.entitiesToDtos(mentionsTweetList);
	}

	@Override
	public List<UserResponseDto> getAllUserFollowers(String username) {
		User userWithFollowers = getUserByUsername(username);
		List<User> userFollowers = userWithFollowers.getFollowers();
		List<User> newUserFollowers = new ArrayList<>();

		for (User user : userFollowers) {
			if (!user.isDeleted()) {
				newUserFollowers.add(user);
			}
		}

		return userMapper.entitiesToDtos(userRepository.saveAllAndFlush(newUserFollowers));
	}

	@Override
	public List<UserResponseDto> getAllUsersFollowed(String username) {
		User userWithFollowings = getUserByUsername(username);
		List<User> userFollowings = userWithFollowings.getFollowing();
		List<User> newUserFollowings = new ArrayList<>();

		for (User user : userFollowings) {
			if (!user.isDeleted()) {
				newUserFollowings.add(user);
			}
		}

		return userMapper.entitiesToDtos(userRepository.saveAllAndFlush(userFollowings));
	}

}
