package com.cooksys.assessment_1.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cooksys.assessment_1.entities.Hashtag;
import com.cooksys.assessment_1.entities.Tweet;

@Repository
public interface HashtagRepository extends JpaRepository<Hashtag, Long>{
	
	Optional<Hashtag> findByLabelAndDeletedFalse(String label);
	
	List<Hashtag> findAllByDeletedFalse();
	
	Hashtag findByLabel(String label);
	
	
}
