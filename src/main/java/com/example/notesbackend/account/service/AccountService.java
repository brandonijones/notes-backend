package com.example.notesbackend.account.service;

//import com.example.notesbackend.account.repository.AccountRepository;
import com.example.notesbackend.account.model.Account;
import com.example.notesbackend.account.model.Role;
import com.example.notesbackend.account.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * Service class for account management.
 */
public interface AccountService {

    /**
     *
     * @return
     */
    UserDetailsService userDetailsService();

}
