package com.example.notesbackend;

import com.example.notesbackend.account.model.Account;
import com.example.notesbackend.account.model.Role;
import com.example.notesbackend.account.repository.AccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class NotesBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotesBackendApplication.class, args);
	}

	@Bean
	CommandLineRunner run(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			if (accountRepository.findByUsername("admin").isPresent()) {
				return;
			}

			Account admin = Account.builder()
					.username("admin")
					.firstName("Jon")
					.lastName("Doe")
					.email("admin@email.com")
					.password(passwordEncoder.encode("password"))
					.role(Role.ADMIN)
					.build();

			accountRepository.save(admin);

		};
	}

}
