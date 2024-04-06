package com.pwdmgr.controller;

import com.pwdmgr.model.Container;
import com.pwdmgr.model.User;
import com.pwdmgr.repository.ContainerRepository;
import com.pwdmgr.repository.UserRepository;
import com.pwdmgr.security.entity.AuthResponse;
import com.pwdmgr.security.service.JwtService;
import com.pwdmgr.security.service.TokenBlacklistService;
import com.pwdmgr.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;
    private final ContainerRepository containerRepository;
    private final UserRepository userRepository;
    private final TokenBlacklistService tokenBlacklistService;


    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody User user) {
        try {
            userService.doSignup(user);
        } catch (Exception e) {
            user.setUsername("USER_ALREADY_EXIST");
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping("/get-all-container")
    public ResponseEntity<List<Container>> getAllContainers(HttpServletRequest request) {
        String username=jwtService.extractUsername(
                request.getHeader("Authorization").substring(7)
        );
        Optional<User> user=userRepository.findByUsername(username);
        return ResponseEntity.ok(containerRepository.findByUser(user.get()));
    }

    @PostMapping("/logout")
    public ResponseEntity<AuthResponse> logout(@RequestHeader("Authorization") String token) {
        System.out.println("inside logout");
        token=token.substring(7);
        tokenBlacklistService.blacklistToken(token);
        return ResponseEntity.ok(new AuthResponse(null,null, "LOGOUT_SUCCESSFULLY"));
    }
}
