package com.example.notesbackend.account.service.impl;

import com.example.notesbackend.account.model.Account;
import com.example.notesbackend.account.repository.AccountRepository;
import com.example.notesbackend.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    public UserDetailsService userDetailsService() {
        return (username) -> {
            Optional<Account> account = accountRepository.findByEmail(username);
            if (account.isEmpty()) {
                account = accountRepository.findByUsername(username);
                if (account.isEmpty()) {
                    throw new UsernameNotFoundException("User " + username + " was not found");
                }
            }
            return account.get();
        };
    }

}
