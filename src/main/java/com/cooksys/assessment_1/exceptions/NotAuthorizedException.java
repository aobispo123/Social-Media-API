package com.cooksys.assessment_1.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NotAuthorizedException extends RuntimeException{/**
	 * 
	 */
	private static final long serialVersionUID = 3523457678759705009L;
	
	private String message;

}
