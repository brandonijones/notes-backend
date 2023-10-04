package com.example.notesbackend.account.service;

import com.example.notesbackend.account.model.Account;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

/**
 *
 */
public interface JwtService {

    /**
     *
     * @param token
     * @return
     */
    String extractUsername(String token);

    /**
     *
     * @param account
     * @return
     */
    String generateToken(Account account);

    /**
     *
     * @param extraClaims
     * @param account
     * @return
     */
    String generateToken(Map<String, Object> extraClaims, Account account);

    /**
     *
     * @param token
     * @param account
     * @return
     */
    boolean isTokenValid(String token, Account account);
}
