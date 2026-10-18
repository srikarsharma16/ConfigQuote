package com.configquote.configquote_api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.configquote.configquote_api.entity.AppUser;
import com.configquote.configquote_api.repository.AppUserRepository;

@Configuration
public class AdminBootstrap {

	@Bean
	CommandLineRunner bootstrapAdmin(AppUserRepository repository, PasswordEncoder encoder,
			@Value("${app.bootstrap.admin.enabled:false}") boolean enabled,
			@Value("${app.bootstrap.admin.username:}") String username,
			@Value("${app.bootstrap.admin.password:}") String password) {

		return args -> {

			if (!enabled) {
				return;
			}

			if (username.isBlank() || password.length() < 12) {
				throw new IllegalStateException(
						"Bootstrap admin needs a username " + "and password of at least 12 characters");
			}

			if (repository.existsByUsername(username)) {
				return;
			}

			AppUser admin = new AppUser();
			admin.setUsername(username);
			admin.setPasswordHash(encoder.encode(password));
			admin.setRole("ADMIN");
			admin.setEnabled(true);

			repository.save(admin);
		};
	}
}
