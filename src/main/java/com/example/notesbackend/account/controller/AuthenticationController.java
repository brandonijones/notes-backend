package com.example.notesbackend.account.controller;

import com.example.notesbackend.account.dto.response.JwtAuthenticationResponse;
import com.example.notesbackend.account.dto.request.LoginRequest;
import com.example.notesbackend.account.dto.request.RegistrationRequest;
import com.example.notesbackend.account.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller mappings for user authentication (login, registration, logout)
 */
@RestController
@RequestMapping(value = "/api/v1/auth")
@CrossOrigin("*")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<JwtAuthenticationResponse> registerAccount(@RequestBody RegistrationRequest body) {
        return new ResponseEntity<>(authenticationService.signup(body), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponse> login(@RequestBody LoginRequest body) {
        return new ResponseEntity<>(authenticationService.login(body), HttpStatus.OK);
    }

}
