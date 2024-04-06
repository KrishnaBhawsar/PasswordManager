package com.pwdmgr.controller;

import com.pwdmgr.security.entity.AuthResponse;
import com.pwdmgr.security.entity.LoginRequest;
import com.pwdmgr.security.service.AuthenticationService;
import com.pwdmgr.security.service.JwtService;
import com.pwdmgr.security.service.TokenBlacklistService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        System.out.println("Login: "+loginRequest.getUsername());
        try {
            authenticationService.doAuthenticate(loginRequest.getUsername(),
                    loginRequest.getPassword());
        } catch (UsernameNotFoundException e) {
            System.out.println("USER_NOT_EXIST");
            return ResponseEntity.ok(new AuthResponse(null,null,"USER_NOT_EXIST"));
        } catch (BadCredentialsException e) {
            System.out.println("INVALID_CREDENTIALS");
            return ResponseEntity.ok(new AuthResponse(null,null,"INVALID_CREDENTIALS"));
        }
        UserDetails userDetails=userDetailsService.loadUserByUsername(loginRequest.getUsername());
        String token=jwtService.generateToken(userDetails);
        AuthResponse authResponse=AuthResponse.builder()
                .token(token)
                .email(userDetails.getUsername()).build();
        return ResponseEntity.ok(authResponse);
    }
}
