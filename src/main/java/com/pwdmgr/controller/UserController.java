package com.pwdmgr.controller;

import com.pwdmgr.model.Container;
import com.pwdmgr.model.User;
import com.pwdmgr.repository.ContainerRepository;
import com.pwdmgr.repository.UserRepository;
import com.pwdmgr.security.entity.AuthResponse;
import com.pwdmgr.security.service.AuthenticationService;
import com.pwdmgr.security.service.JwtService;
import com.pwdmgr.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final ContainerRepository containerRepository;
    private final UserRepository userRepository;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@RequestBody User user) {
        try {
            userService.doSignup(user);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.ok(AuthResponse.builder().token(null).message("username already exist").build());
        }
        UserDetails userDetails=userDetailsService.loadUserByUsername(user.getUsername());
        String token=jwtService.generateToken(userDetails);
        AuthResponse authResponse=AuthResponse.builder()
                .token(token)
                .email(userDetails.getUsername()).build();
        return ResponseEntity.ok(authResponse);
    }

    @GetMapping("/get-all-container")
    public ResponseEntity<List<Container>> getAllContainers(HttpServletRequest request) {
        String username=jwtService.extractUsername(
                request.getHeader("Authorization").substring(7)
        );
        Optional<User> user=userRepository.findByUsername(username);
        return ResponseEntity.ok(containerRepository.findByUser(user.get()));
    }
}
