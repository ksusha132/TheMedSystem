package project.security.services;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import project.model.Role;
import project.repository.UserRepository;

@Service
public class CustomUserDetailService implements UserDetailsService {

	// get user from the database, via Hibernate
	@Autowired
	private UserRepository userRepository;

	@Transactional(readOnly = true)
	@Override
	public User loadUserByUsername(final String username) throws UsernameNotFoundException {
		System.out.println("start loading user from db by login: " + username);
		
		project.model.User user = userRepository.findByLogin(username);
		
		if (user == null) throw new UsernameNotFoundException("Incorrect login");
		
		System.out.println("user = " + user);
		GrantedAuthority role = new SimpleGrantedAuthority("ROLE_" + user.getRole().getName().toUpperCase());
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(role);

		return new User(username, user.getPassword(), user.getEnabled(), true, true, true, authorities);
	}

}
