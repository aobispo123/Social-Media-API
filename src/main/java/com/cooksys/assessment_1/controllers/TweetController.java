package com.cooksys.assessment_1.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.assessment_1.dtos.ContextDto;
import com.cooksys.assessment_1.dtos.CredentialsDto;
import com.cooksys.assessment_1.dtos.HashtagResponseDto;
import com.cooksys.assessment_1.dtos.TweetRequestDto;
import com.cooksys.assessment_1.dtos.TweetResponseDto;
import com.cooksys.assessment_1.dtos.UserResponseDto;
import com.cooksys.assessment_1.services.TweetService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tweets")
public class TweetController {
    
    private final TweetService tweetService;

    @GetMapping
    public List<TweetResponseDto> getAllTweets() {
        return tweetService.getAllTweets();
    }

    @PostMapping
    public TweetResponseDto createTweet(@RequestBody TweetRequestDto tweetRequestDto) {
        return tweetService.createTweet(tweetRequestDto);
    }

    @GetMapping("/{id}")
    public TweetResponseDto getTweetById(@PathVariable Long id) {
        return tweetService.getTweetById(id);
    }

    @DeleteMapping("/{id}")
    public TweetResponseDto deleteTweetById(@PathVariable Long id, @RequestBody CredentialsDto credentialsDto) {
        return tweetService.deleteTweetById(id, credentialsDto);
    }

    @PostMapping("/{id}/like")
    public void likeTweet(@PathVariable Long id, @RequestBody CredentialsDto credentialsDto) {
        tweetService.likeTweet(id, credentialsDto);
    }

    @PostMapping("/{id}/reply")
    public TweetResponseDto replyToTweet(@PathVariable Long id, @RequestBody TweetRequestDto tweetRequestDto) {
        return tweetService.replyToTweet(id, tweetRequestDto);
    }

    @PostMapping("/{id}/repost")
    public TweetResponseDto repostTweet(@PathVariable Long id, @RequestBody CredentialsDto credentialsDto) {
        return tweetService.repostTweet(id, credentialsDto);
    }

    @GetMapping("/{id}/tags")
    public List<HashtagResponseDto> getAllHashtagsByTweetId(@PathVariable Long id) {
        return tweetService.getAllHashtagsByTweetId(id);
    }

    @GetMapping("/{id}/likes")
    public List<UserResponseDto> getAllLikesByTweetId(@PathVariable Long id) {
        return tweetService.getAllLikesByTweetId(id);
    }

    @GetMapping("/{id}/context")
    public ContextDto getContextByTweetId(@PathVariable Long id) {
        return tweetService.getContextByTweetId(id);
    } 

    @GetMapping("/{id}/replies")
    public List<TweetResponseDto> getRepliesByTweetId(@PathVariable Long id) {
        return tweetService.getRepliesByTweetId(id);
    }

    @GetMapping("/{id}/reposts")
    public List<TweetResponseDto> getRepostsByTweetId(@PathVariable Long id) {
        return tweetService.getRepostsByTweetId(id);
    }

    @GetMapping("/{id}/mentions")
    public List<UserResponseDto> getMentionsByTweetId(@PathVariable Long id) {
        return tweetService.getMentionsByTweetId(id);
    }
}
