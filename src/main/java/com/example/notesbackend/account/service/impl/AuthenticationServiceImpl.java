package com.example.notesbackend.account.service.impl;

import com.example.notesbackend.account.dto.response.JwtAuthenticationResponse;
import com.example.notesbackend.account.dto.request.LoginRequest;
import com.example.notesbackend.account.dto.request.RegistrationRequest;
import com.example.notesbackend.account.model.Account;
import com.example.notesbackend.account.model.Role;
import com.example.notesbackend.account.model.Token;
import com.example.notesbackend.account.model.TokenType;
import com.example.notesbackend.account.repository.AccountRepository;
import com.example.notesbackend.account.repository.TokenRepository;
import com.example.notesbackend.account.service.AuthenticationService;
import com.example.notesbackend.account.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AccountRepository accountRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public JwtAuthenticationResponse signup(RegistrationRequest request) {
        Account newAccount = Account.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        Account savedAccount = accountRepository.save(newAccount);
        String jwt = jwtService.generateToken(savedAccount);
        saveAccountToken(savedAccount, jwt);
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
        revokeAllAccountTokens(account.get());
        saveAccountToken(account.get(), jwt);
        return JwtAuthenticationResponse.builder()
                .token(jwt)
                .message("Login successful!")
                .build();
    }

    private void revokeAllAccountTokens(Account account) {
        List<Token> validAccountTokens = tokenRepository.findAllValidTokensByAccount(account.getId());
        if (validAccountTokens.isEmpty()) {
            log.debug("No existing valid tokens found for account: " + account.getUsername());
            return;
        }

        validAccountTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
            token.setUpdateTime(LocalDateTime.now());
        });

        tokenRepository.saveAll(validAccountTokens);
    }

    private void saveAccountToken(Account account, String jwt) {
        Token token = Token.builder()
                .token(jwt)
                .account(account)
                .tokenType(TokenType.BEARER)
                .creationTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

}
