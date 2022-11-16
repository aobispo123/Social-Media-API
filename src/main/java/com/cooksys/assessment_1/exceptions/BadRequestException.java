package com.cooksys.assessment_1.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BadRequestException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1625092036497419311L;
	
	private String message;
}
