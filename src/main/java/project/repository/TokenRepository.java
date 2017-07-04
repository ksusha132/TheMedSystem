package project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import project.security.model.Token;

public interface TokenRepository extends JpaRepository<Token, String> {
	
//	List<Token> findByUserLogin(String userLogin);
	
}