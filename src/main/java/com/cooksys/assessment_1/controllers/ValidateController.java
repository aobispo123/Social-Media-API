package com.cooksys.assessment_1.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.assessment_1.services.ValidateService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/validate")
public class ValidateController {
	
	private final ValidateService validateService;
	
	@GetMapping("/tag/exists/{label}")
	public boolean checkHashtag(@PathVariable String label) {
		return validateService.checkHashtag(label);
	}
	
	@GetMapping("/username/exists/@{username}")  
	public boolean checkUsernameExists(@PathVariable String username) {
		return validateService.checkUsernameExists(username);
	}
	
	@GetMapping("/username/available/@{username}")
	public boolean checkUsernameAvailable(@PathVariable String username) {
		return validateService.checkUsernameAvailable(username);
	}
}
