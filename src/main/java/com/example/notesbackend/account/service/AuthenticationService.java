package com.example.notesbackend.account.service;

import com.example.notesbackend.account.dto.response.JwtAuthenticationResponse;
import com.example.notesbackend.account.dto.request.LoginRequest;
import com.example.notesbackend.account.dto.request.RegistrationRequest;

public interface AuthenticationService {

    /**
     *
     * @param request
     * @return
     */
    JwtAuthenticationResponse signup(RegistrationRequest request);

    /**
     *
     * @param request
     * @return
     */
    JwtAuthenticationResponse login(LoginRequest request);

}
