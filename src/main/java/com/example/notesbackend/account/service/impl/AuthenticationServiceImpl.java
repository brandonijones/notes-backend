package com.example.notesbackend.account.service.impl;

import com.example.notesbackend.account.dto.response.JwtAuthenticationResponse;
import com.example.notesbackend.account.dto.request.LoginRequest;
import com.example.notesbackend.account.dto.request.RegistrationRequest;
import com.example.notesbackend.account.model.Account;
import com.example.notesbackend.account.model.Role;
import com.example.notesbackend.account.repository.AccountRepository;
import com.example.notesbackend.account.service.AuthenticationService;
import com.example.notesbackend.account.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public JwtAuthenticationResponse signup(RegistrationRequest request) {
        Account account = Account.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        accountRepository.save(account);
        String jwt = jwtService.generateToken(account);
        return JwtAuthenticationResponse.builder()
                .token(jwt)
                .message("Registration successful!")
                .build();
    }

    @Override
    public JwtAuthenticationResponse login(LoginRequest request) {
        Optional<Account> account = accountRepository.findByUsername(request.getUsername());
        if (account.isEmpty()) {
            account = accountRepository.findByEmail(request.getUsername());
            if (account.isEmpty()) {
                throw new IllegalArgumentException("Invalid username or email.");
            }
            request.setUsername(account.get().getUsername());
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        String jwt = jwtService.generateToken(account.get());
        return JwtAuthenticationResponse.builder()
                .token(jwt)
                .message("Login successful!")
                .build();
    }

}
