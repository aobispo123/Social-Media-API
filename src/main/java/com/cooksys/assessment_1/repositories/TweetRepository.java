package com.cooksys.assessment_1.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cooksys.assessment_1.entities.Tweet;

@Repository
public interface TweetRepository extends JpaRepository<Tweet, Long> {
    
    Optional<Tweet> findById(Long id);

    //Michael added this for getAllUserMentions
	List<Tweet> findAllByDeletedFalse();

	//Michael added this for getContextByTweetId
	Optional<Tweet> findByIdAndDeletedFalse(Long id); 
       
}
