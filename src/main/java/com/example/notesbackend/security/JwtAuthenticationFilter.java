package com.example.notesbackend.security;

import com.example.notesbackend.account.model.Account;
import com.example.notesbackend.account.service.AccountService;
import com.example.notesbackend.account.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String AUTH_HEADER_START = "Bearer ";
    private final JwtService jwtService;
    private final AccountService accountService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authenticationHeader = request.getHeader(AUTHORIZATION_HEADER);
        final String jwt;
        final String username;
        if (StringUtils.isEmpty(authenticationHeader) || !StringUtils.startsWith(authenticationHeader, AUTH_HEADER_START)) {
            filterChain.doFilter(request, response);
            return;
        }
        jwt = authenticationHeader.substring(AUTH_HEADER_START.length());
        username = jwtService.extractUsername(jwt);
        if (StringUtils.isNoneEmpty(username)
                && SecurityContextHolder.getContext().getAuthentication() == null) {
            Account account = (Account) this.accountService.userDetailsService().loadUserByUsername(username);
            if (jwtService.isTokenValid(jwt, account)) {
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        account, null, account.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                context.setAuthentication(authToken);
                SecurityContextHolder.setContext(context);
            }
        }
        filterChain.doFilter(request, response);
    }

}
