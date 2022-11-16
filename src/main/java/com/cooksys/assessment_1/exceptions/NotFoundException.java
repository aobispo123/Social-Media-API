package com.cooksys.assessment_1.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NotFoundException extends RuntimeException{/**
	 * 
	 */
	private static final long serialVersionUID = 1409035362531648153L;
	
	private String message;

}
