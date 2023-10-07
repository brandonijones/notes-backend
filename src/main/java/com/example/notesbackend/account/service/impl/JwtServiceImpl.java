package com.example.notesbackend.account.service.impl;

import com.example.notesbackend.account.model.Account;
import com.example.notesbackend.account.model.Token;
import com.example.notesbackend.account.repository.TokenRepository;
import com.example.notesbackend.account.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * Implementation of the JWT Service.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    private static final int TWENTY_FOUR_HOURS = 1000 * 60 * 24;

    @Value("${app.jwt-secret}")
    private String SECRET_KEY;

    private final TokenRepository tokenRepository;

    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public String generateToken(Account account) {
        return generateToken(new HashMap<>(), account);
    }

    @Override
    public String generateToken(Map<String, Object> extraClaims, Account account) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(account.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TWENTY_FOUR_HOURS))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public boolean isTokenValid(String token, Account account) {
        final String username = extractUsername(token);
        Optional<Token> tokenObject = tokenRepository.findByToken(token);
        return tokenObject.isPresent()
                && username.equals(account.getUsername())
                && !isTokenExpiredOrRevoked(tokenObject.get(), token);
    }

    /**
     *
     * @param token
     * @param claimsResolver
     * @return
     * @param <T>
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private boolean isTokenExpiredOrRevoked(Token tokenObject, String token) {
        if (isTokenExpired(token) || tokenObject.isRevoked()) {
            tokenObject.setExpired(true);
            tokenObject.setRevoked(true);
            tokenRepository.save(tokenObject);
            log.debug("JWT is expired or revoked");
            return true;
        }

        log.debug("JWT is not expired or revoked");
        return false;
    }

    /**
     *
     * @param token
     * @return
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     *
     * @param token
     * @return
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     *
     * @param token
     * @return
     */
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     *
     * @return
     */
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
