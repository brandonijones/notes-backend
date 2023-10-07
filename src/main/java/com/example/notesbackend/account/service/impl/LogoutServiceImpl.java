package com.example.notesbackend.account.service.impl;

import com.example.notesbackend.account.model.Token;
import com.example.notesbackend.account.repository.TokenRepository;
import com.example.notesbackend.account.service.LogoutService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 *
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LogoutServiceImpl implements LogoutService {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String AUTH_HEADER_START = "Bearer ";

    private final TokenRepository tokenRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String authenticationHeader = request.getHeader(AUTHORIZATION_HEADER);
        final String jwt;
        if (StringUtils.isEmpty(authenticationHeader) || !StringUtils.startsWith(authenticationHeader, AUTH_HEADER_START)) {
            return;
        }
        jwt = authenticationHeader.substring(AUTH_HEADER_START.length());
        Optional<Token> storedToken = tokenRepository.findByToken(jwt);
        if (storedToken.isPresent()) {
            Token tokenToUpdate = storedToken.get();
            tokenToUpdate.setExpired(true);
            tokenToUpdate.setRevoked(true);
            tokenRepository.save(tokenToUpdate);
        }
    }

}
