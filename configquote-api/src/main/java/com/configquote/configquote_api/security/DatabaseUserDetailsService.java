package com.configquote.configquote_api.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.configquote.configquote_api.entity.AppUser;
import com.configquote.configquote_api.repository.AppUserRepository;

@Service
public class DatabaseUserDetailsService implements UserDetailsService {

	private final AppUserRepository userRepository;

	public DatabaseUserDetailsService(AppUserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) {

		AppUser user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Invalid username or password"));

		return User.withUsername(user.getUsername()).password(user.getPasswordHash()).roles(user.getRole())
				.disabled(!user.isEnabled()).build();
	}
}
