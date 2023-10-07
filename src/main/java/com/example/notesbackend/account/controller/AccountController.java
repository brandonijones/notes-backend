package com.example.notesbackend.account.controller;

import com.example.notesbackend.account.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller mappings for account management.
 */
@RestController
@RequestMapping(value = "/api/v1/account")
@CrossOrigin("*")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return new ResponseEntity<>("Hello User!", HttpStatus.OK);
    }

}
