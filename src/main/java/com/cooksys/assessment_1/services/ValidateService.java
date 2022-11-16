package com.cooksys.assessment_1.services;

public interface ValidateService {

	boolean checkHashtag(String label);

	boolean checkUsernameExists(String username);

	boolean checkUsernameAvailable(String username);

}
