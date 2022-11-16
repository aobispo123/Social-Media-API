package com.cooksys.assessment_1.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cooksys.assessment_1.entities.Credentials;
import com.cooksys.assessment_1.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

	Optional<User> findByIdAndDeletedFalse(Long id);
	
	List<User> findAllByDeletedFalse();

	User findByCredentials(Credentials credentials);

	User findByCredentialsUsername(String username);
}
