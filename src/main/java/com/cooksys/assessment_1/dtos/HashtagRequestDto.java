package com.cooksys.assessment_1.dtos;

import java.sql.Timestamp;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class HashtagRequestDto {
	
	//Only need Hashtag Dto 
	private String label;
	
	private Timestamp firstUsed;
	
	private Timestamp lastUsed;
	
}
