package com.example.notesbackend.account.dto.response;

import lombok.Builder;
import lombok.Data;

/**
 *
 */
@Data
@Builder
public class JwtAuthenticationResponse {

    private String message;
    private String token;

}
