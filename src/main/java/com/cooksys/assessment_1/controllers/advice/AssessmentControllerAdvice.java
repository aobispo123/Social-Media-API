package com.cooksys.assessment_1.controllers.advice;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.cooksys.assessment_1.dtos.ErrorDto;
import com.cooksys.assessment_1.exceptions.BadRequestException;
import com.cooksys.assessment_1.exceptions.NotAuthorizedException;
import com.cooksys.assessment_1.exceptions.NotFoundException;


@ControllerAdvice (basePackages = {"com.cooksys.assessment_1.controllers"})
@ResponseBody
public class AssessmentControllerAdvice {
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(BadRequestException.class)
	public ErrorDto handleBadRequestException(HttpServletRequest request, BadRequestException exception) {
		return new ErrorDto(exception.getMessage());
	}
	
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ExceptionHandler(NotAuthorizedException.class)
	public ErrorDto handleNotAuthorizedException(HttpServletRequest request, NotAuthorizedException exception) {
		return new ErrorDto(exception.getMessage());
	}
	
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(NotFoundException.class)
	public ErrorDto handleNotFoundException(HttpServletRequest request, NotFoundException exception) {
		return new ErrorDto(exception.getMessage());
	}
}
