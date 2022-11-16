package com.cooksys.assessment_1.services.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cooksys.assessment_1.dtos.HashtagResponseDto;
import com.cooksys.assessment_1.dtos.TweetResponseDto;
import com.cooksys.assessment_1.entities.Hashtag;
import com.cooksys.assessment_1.entities.Tweet;
import com.cooksys.assessment_1.entities.User;
import com.cooksys.assessment_1.exceptions.NotFoundException;
import com.cooksys.assessment_1.mappers.HashtagMapper;
import com.cooksys.assessment_1.mappers.TweetMapper;
import com.cooksys.assessment_1.repositories.HashtagRepository;
import com.cooksys.assessment_1.repositories.TweetRepository;
import com.cooksys.assessment_1.services.HashtagService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HashtagServiceImpl implements HashtagService {

	private final HashtagMapper hashtagMapper;
	private final HashtagRepository hashtagRepository;
	private final TweetMapper tweetMapper;
	private final TweetRepository tweetRepository;

	// ASSIGNED REQUIREMENT:
	// Retrieves all hashtags tracked by the database.
	// Response ['Hashtag']
	@Override
	public List<HashtagResponseDto> getAllHashtags() {
		return hashtagMapper.entitiesToDtos(hashtagRepository.findAll());
	}

	// ASSIGNED REQUIREMENT:
	// Retrieves all (non-deleted) tweets tagged with the given hashtag label.
	// The tweets should appear in reverse-chronological order. If no hashtag with
	// the given
	// label exists, an error should be sent in lieu of a response.
	// A tweet is considered "tagged" by a hashtag if the tweet has content and the
	// hashtag's label appears in that content following a #
	// Response: ['Tweet']
	@Override
	public List<TweetResponseDto> getTweetsByHashtag(String label) {

		// get all labels
		List<Hashtag> allHashtags = hashtagRepository.findAll();
		List<String> allLabelsList = new ArrayList<>();
		for (Hashtag hashtag : allHashtags) {
			allLabelsList.add(hashtag.getLabel());
		}

		// check all labels for argument label
		boolean hasLabel = false;
		for (String labelElement : allLabelsList) {
//			if (labelElement.equals("#" + label)) {
			if (labelElement.equals(label)) {
				hasLabel = true;
			}
		}
		if (!hasLabel) {
			throw new NotFoundException("No hashtag found with label: " + label);
		}

		// get all tweets with label
		List<Tweet> tweets = tweetRepository.findAllByDeletedFalse();
		List<Tweet> taggedTweets = new ArrayList<>();
		for (Tweet tweet : tweets) {
//			if (tweet.getContent() != null && tweet.getContent().contains("#" + label)) {
			if (tweet.getContent() != null && tweet.getContent().contains(label)) {
				
				taggedTweets.add(tweet);
			}
		}
		Collections.sort(taggedTweets, Comparator.comparing(Tweet::getPosted));
		Collections.reverse(taggedTweets);
		return tweetMapper.entitiesToDtos(taggedTweets);

	}

}
