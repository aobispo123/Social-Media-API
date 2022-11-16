package com.cooksys.assessment_1.services.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.cooksys.assessment_1.dtos.ContextDto;
import com.cooksys.assessment_1.dtos.CredentialsDto;
import com.cooksys.assessment_1.dtos.HashtagResponseDto;
import com.cooksys.assessment_1.dtos.TweetRequestDto;
import com.cooksys.assessment_1.dtos.TweetResponseDto;
import com.cooksys.assessment_1.dtos.UserResponseDto;
import com.cooksys.assessment_1.entities.Credentials;
import com.cooksys.assessment_1.entities.Hashtag;
import com.cooksys.assessment_1.entities.Tweet;
import com.cooksys.assessment_1.entities.User;
import com.cooksys.assessment_1.exceptions.BadRequestException;
import com.cooksys.assessment_1.exceptions.NotAuthorizedException;
import com.cooksys.assessment_1.exceptions.NotFoundException;
import com.cooksys.assessment_1.mappers.CredentialsMapper;
import com.cooksys.assessment_1.mappers.HashtagMapper;
import com.cooksys.assessment_1.mappers.TweetMapper;
import com.cooksys.assessment_1.mappers.UserMapper;
import com.cooksys.assessment_1.repositories.HashtagRepository;
import com.cooksys.assessment_1.repositories.TweetRepository;
import com.cooksys.assessment_1.repositories.UserRepository;
import com.cooksys.assessment_1.services.TweetService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TweetServiceImpl implements TweetService {

	private final TweetMapper tweetMapper;
	private final UserMapper userMapper;
	private final CredentialsMapper credentialsMapper;
	private final HashtagMapper hashtagMapper;

	private final TweetRepository tweetRepository;
    private final UserRepository userRepository;
    private final HashtagRepository hashtagRepository;

    // ------------------------------------
    // --------- Helper Methods -----------
    // ------------------------------------

    // Return a tweet from tweetRepo by ID, if there's an error throw exception
    private Tweet getTweet(Long id) {
        Optional<Tweet> optionalTweet = tweetRepository.findById(id);
        if (optionalTweet.isEmpty())
            throw new NotFoundException("Tweet of ID: " + id + " is not found.");
        return optionalTweet.get();
      }

    // Check tweet isDeleted(), if true throw exception
    private Tweet throwExceptionIfTweetDeleted(Tweet tweet) {
    	if (tweet.isDeleted()) throw new NotFoundException("Tweet of ID: " + tweet.getId() + " is not found.");
    	return tweet;
    }

    // Check user for null or isDeleted(), if true throw exception
    private User throwExceptionIfUserNullOrDeleted(User user) {
    	if (user == null || user.isDeleted() == true)
        	throw new NotFoundException("Incorrect username or password.");
    	return user;
    }

    // Removes deleted Tweets from a list and returns the updated list
    private List<Tweet> removeDeletedTweets(List<Tweet> tweets) {
        List<Tweet> tweetsWithNoDeletes = new ArrayList<>();
        for (Tweet tweet : tweets)
            if (!tweet.isDeleted())
                tweetsWithNoDeletes.add(tweet);
        return tweetsWithNoDeletes;
    }

    // Parses tweet content and handles Hashtags and User Mentions
    private Tweet parseHashtagsAndMentions(Tweet createdTweet) {
        // Check tweet content for hashtags
        if (createdTweet.getContent().contains("#"))
        {
            // Use regex to isolate hashtag pattern, compile and use matcher
                       
            String patternStr = "(#+[a-zA-Z0-9(_)]{1,})";
            Pattern pattern = Pattern.compile(patternStr);
            Matcher matcher = pattern.matcher(createdTweet.getContent());

            // create List<String> and populate with hashtags found
            List<String> hashtagsInContent = new ArrayList<>();
            while (matcher.find()) hashtagsInContent.add(matcher.group().substring(1, matcher.group().length()));    
//            while (matcher.find()) hashtagsInContent.add(matcher.group());
            
            // Check hashtags against the hashtagRepo, and add if they don't exist
            for (String hashtag : hashtagsInContent)
            {
                // Is it bad to use the new variable declaration here, instead of
                // declaring it above the forloop with a setter inside the loop?
                Hashtag newHashtag = hashtagRepository.findByLabel(hashtag);

                // If the hashtag doesn't exist in the hashtagRepo, create a new one and save
                if (newHashtag == null)
                {
                    newHashtag = new Hashtag();
                    newHashtag.setLabel(hashtag);
                    hashtagRepository.saveAndFlush(newHashtag);
                }

                // Add hashtag to the tweet's hashtag list, if list is null create a new list
                if (createdTweet.getHashtags() == null) createdTweet.setHashtags(new ArrayList<>());
                createdTweet.getHashtags().add(newHashtag);
            }
        }

        // Check tweet.content for user mentions
        if (createdTweet.getContent().contains("@"))
        {
            // Use regex to isolate user mentions
            String patternStr = "(@+[a-zA-Z0-9(_)]{1,})";
            Pattern pattern = Pattern.compile(patternStr);
            Matcher matcher = pattern.matcher(createdTweet.getContent());

            // create List<String> to populate with mentioned users found
            List<String> userMentionsInContent = new ArrayList<>();
            while (matcher.find()) userMentionsInContent.add(matcher.group().replace("@", ""));

            for (String username : userMentionsInContent)
            {
                // Check username against the userRepo to see if it exists, grab if so
                User userFromRepo = userRepository.findByCredentialsUsername(username);

                // If the user isn't null AND deleted (I think this helps prevent null errors when checking
                // the user.isDeleted() if user is null?)
                if (userFromRepo != null && userFromRepo.isDeleted() == false)
                {
                    // Add user to the tweet's mentioned users list, if list is null create a new list
                    if (createdTweet.getMentionedUsers() == null) createdTweet.setMentionedUsers(new ArrayList<>());
                    createdTweet.getMentionedUsers().add(userFromRepo);
                }
            }
        }

        return createdTweet;
    }

    // ------------------------------------
    // ----------- End Points -------------
    // ------------------------------------
	
    @Override
    public List<TweetResponseDto> getAllTweets() {
        List<Tweet> allTweets = removeDeletedTweets(tweetRepository.findAll());
        allTweets.sort((e1, e2) -> e2.getPosted().compareTo(e1.getPosted()));
        
        return tweetMapper.entitiesToDtos(allTweets);
    }

    @Override
    public TweetResponseDto createTweet(TweetRequestDto tweetRequestDto) {
        // Check credentials against the repo and grab user if it exists, check for exceptions
        Credentials incomingCredentials = credentialsMapper.dtoToEntity(tweetRequestDto.getCredentials());
        User user = throwExceptionIfUserNullOrDeleted(userRepository.findByCredentials(incomingCredentials));

        Tweet createdTweet = tweetMapper.DtoToEntity(tweetRequestDto);
        createdTweet.setAuthor(user);

        // Content is required for this tweet
        if (createdTweet.getContent() == null || createdTweet.getContent().length() == 0)
            throw new BadRequestException("Tweet must contain content.");
        
        // Parse the content for hashtags and mentions, then save and flush
        return tweetMapper.entityToDto(tweetRepository.saveAndFlush(parseHashtagsAndMentions(createdTweet)));
    }
    
    @Override
    public TweetResponseDto getTweetById(Long id) {
        return tweetMapper.entityToDto(throwExceptionIfTweetDeleted(getTweet(id)));
    }

    @Override
    public TweetResponseDto deleteTweetById(Long id, CredentialsDto credentialsDto) {
        Tweet tweet = throwExceptionIfTweetDeleted(getTweet(id));
        Credentials incomingCredentials = credentialsMapper.dtoToEntity(credentialsDto);

        if (!tweet.getAuthor().getCredentials().equals(incomingCredentials))
            throw new NotAuthorizedException("Invalid login information to make this request.");

        tweet.setDeleted(true);
        return tweetMapper.entityToDto(tweetRepository.saveAndFlush(tweet));
    }
    
    @Override
    public void likeTweet(Long id, CredentialsDto credentialsDto) {
        Tweet tweet = throwExceptionIfTweetDeleted(getTweet(id));
        User user = throwExceptionIfUserNullOrDeleted(userRepository.findByCredentials(credentialsMapper.dtoToEntity(credentialsDto)));

        if(!user.getLikedTweets().contains(tweet)) {
        	user.getLikedTweets().add(tweet);
        }
        
        userRepository.saveAndFlush(user);
    }
        
    @Override
    public TweetResponseDto replyToTweet(Long id, TweetRequestDto tweetRequestDto) {
        Tweet targetTweet = throwExceptionIfTweetDeleted(getTweet(id));
        Tweet replyTweet = tweetMapper.DtoToEntity(tweetRequestDto);
        // Checks request getCredentials() against Repo, grabs user, checks for Exceptions
        User user = throwExceptionIfUserNullOrDeleted(userRepository.findByCredentials(credentialsMapper.dtoToEntity(tweetRequestDto.getCredentials())));
        
        // Content is required for this tweet
        if (replyTweet.getContent() == null || replyTweet.getContent().length() == 0)
            throw new BadRequestException("Tweet must contain content.");

        replyTweet.setAuthor(user);
        replyTweet.setInReplyTo(targetTweet);

        return tweetMapper.entityToDto(tweetRepository.saveAndFlush(parseHashtagsAndMentions(replyTweet)));
    }
        
    @Override
    public TweetResponseDto repostTweet(Long id, CredentialsDto credentialsDto) {
        Tweet targetTweet = throwExceptionIfTweetDeleted(getTweet(id));
        User user = throwExceptionIfUserNullOrDeleted(userRepository.findByCredentials(credentialsMapper.dtoToEntity(credentialsDto)));
        
        Tweet repostTweet = new Tweet();
        repostTweet.setAuthor(user);
        repostTweet.setRepostOf(targetTweet);

        targetTweet.getReposts().add(repostTweet);
        tweetRepository.saveAndFlush(targetTweet);
        
        return tweetMapper.entityToDto(tweetRepository.saveAndFlush(repostTweet));
    }

    @Override
    public List<HashtagResponseDto> getAllHashtagsByTweetId(Long id) {
    	Optional<Tweet> optionalTweet = tweetRepository.findByIdAndDeletedFalse(id);
    	if(optionalTweet.isEmpty()) {
    		throw new NotFoundException("No tweet found with id: " + id);
    	}
    	Tweet targetTweet = optionalTweet.get();
        Tweet tweet = throwExceptionIfTweetDeleted(targetTweet);
        return hashtagMapper.entitiesToDtos(tweet.getHashtags());
    }

    @Override
    public List<UserResponseDto> getAllLikesByTweetId(Long id) {
        List<User> userLikes = new ArrayList<>();
        for (User user : getTweet(id).getLikedByUsers())
            if (!user.isDeleted()) userLikes.add(user);

        return userMapper.entitiesToDtos(userLikes);
    }

//		CONTEXT    
//    The reply context of a tweet. The before property represents the chain of replies that led to the target tweet, 
//    and the after property represents the chain of replies that followed the target tweet.
//	  The chains should be in chronological order, and the after chain should include all replies of replies, 
//    meaning that all branches of replies must be flattened into a single chronological list to fully satisfy the requirements.

	// ToDo: Retrieves the context of the tweet with the given id. If that tweet is
	// deleted or otherwise doesn’t exist,
	// an error should be sent in lieu of a response.
	// IMPORTANT: While deleted tweets should not be included in the before and
	// after properties of the result,
	// transitive replies should. What that means is that if a reply to the target
	// of the context is deleted, but there’s
	// another reply to the deleted reply, the deleted reply should be excluded but
	// the other reply should remain.
	// RESPONSE: 'Context'

	// STATUS: COMPLETE
	// [IMPLEMENTED] If that tweet is deleted or otherwise doesn’t exist, an error should be sent in lieu of a response.
	// [IMPLEMENTED] Excluded deleted tweets 
	// [IMPLEMENTED] Include transitive tweets
	@Override
	public ContextDto getContextByTweetId(Long id) {

		Optional<Tweet> optionalTargetTweet = tweetRepository.findByIdAndDeletedFalse(id);
		if(optionalTargetTweet.isEmpty()) {
			throw new NotFoundException("No tweet found with id: " + id);
		}
		Tweet targetTweetEntity = optionalTargetTweet.get();

		TweetResponseDto targetTweetResponseDto = tweetMapper.entityToDto(optionalTargetTweet.get());
		ContextDto contextDto = new ContextDto();
		contextDto.setTarget(targetTweetResponseDto);

		// get "before" tweets
		List<Tweet> beforeTweetEntities = new ArrayList<>();
		Tweet newTargetTweetEntity = targetTweetEntity;
		while (newTargetTweetEntity.getInReplyTo() != null) {
			newTargetTweetEntity = newTargetTweetEntity.getInReplyTo();
			beforeTweetEntities.add(newTargetTweetEntity);
		}
		Collections.sort(beforeTweetEntities, Comparator.comparing(Tweet::getPosted));
		beforeTweetEntities = removeDeletedTweets(beforeTweetEntities);		
		
		List<TweetResponseDto> beforeTweetResponseDtos = tweetMapper.entitiesToDtos(beforeTweetEntities);
		contextDto.setBefore(beforeTweetResponseDtos);

		// get "after" tweets
		List<Tweet> directRepliesTweets = targetTweetEntity.getReplies();
		Set<Tweet> replySet1 = new HashSet<>(directRepliesTweets);
		Set<Tweet> replySet2 = new HashSet<>();
		while (replySet1 != replySet2) {
			replySet2 = replySet1;
			for (Tweet reply : replySet1) {
				replySet1.addAll(reply.getReplies());
			}
		}
		List<Tweet> afterTweetEntities = new ArrayList<>(replySet1);
		afterTweetEntities = removeDeletedTweets(afterTweetEntities);
		Collections.sort(afterTweetEntities, Comparator.comparing(Tweet::getPosted));
		List<TweetResponseDto> afterTweetResponseDtoList = tweetMapper.entitiesToDtos(afterTweetEntities);
		contextDto.setAfter(afterTweetResponseDtoList);

		return contextDto;

	}

	@Override
	public List<TweetResponseDto> getRepliesByTweetId(Long id) {
		Tweet targetTweet = throwExceptionIfTweetDeleted(getTweet(id));
        return tweetMapper.entitiesToDtos(removeDeletedTweets(targetTweet.getReplies()));
	}

	@Override
	public List<TweetResponseDto> getRepostsByTweetId(Long id) {
		Tweet targetTweet = throwExceptionIfTweetDeleted(getTweet(id));
        return tweetMapper.entitiesToDtos(removeDeletedTweets(targetTweet.getReposts()));
	}

	@Override
	public List<UserResponseDto> getMentionsByTweetId(Long id) {
		Tweet targetTweet = throwExceptionIfTweetDeleted(getTweet(id));
		List<User> tweetMentions = new ArrayList<>();
		for (User user : targetTweet.getMentionedUsers())
			if (!user.isDeleted()) tweetMentions.add(user);
            
        return userMapper.entitiesToDtos(tweetMentions);
	}

}
