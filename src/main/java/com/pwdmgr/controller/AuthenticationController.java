package com.pwdmgr.controller;

import com.pwdmgr.security.entity.AuthResponse;
import com.pwdmgr.security.entity.LoginRequest;
import com.pwdmgr.security.service.AuthenticationService;
import com.pwdmgr.security.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        try {
            authenticationService.doAuthenticate(loginRequest.getUsername(),
                    loginRequest.getPassword());
        } catch (BadCredentialsException e) {
            return ResponseEntity.ok(new AuthResponse(null,null,"Invalid credentials"));
        }
        UserDetails userDetails=userDetailsService.loadUserByUsername(loginRequest.getUsername());
        String token=jwtService.generateToken(userDetails);
        AuthResponse authResponse=AuthResponse.builder()
                .token(token)
                .email(userDetails.getUsername()).build();
        return ResponseEntity.ok(authResponse);

    }
}
