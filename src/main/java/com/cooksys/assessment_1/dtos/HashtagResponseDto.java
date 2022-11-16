package com.cooksys.assessment_1.dtos;

import java.sql.Timestamp;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class HashtagResponseDto {
	
	private Long id;

	private String label;

	private Timestamp firstUsed;

	private Timestamp lastUsed;

}
