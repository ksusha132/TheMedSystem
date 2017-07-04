package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import project.model.User;
import project.model.VerificationToken;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

	VerificationToken findByToken(String token);
	
	VerificationToken findByUser(User user);
}
