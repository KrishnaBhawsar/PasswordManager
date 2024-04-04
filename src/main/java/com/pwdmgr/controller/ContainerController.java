package com.pwdmgr.controller;

import com.pwdmgr.model.Container;
import com.pwdmgr.model.User;
import com.pwdmgr.repository.ContainerRepository;
import com.pwdmgr.repository.UserRepository;
import com.pwdmgr.security.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class ContainerController {

    private final ContainerRepository containerRepository;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @PostMapping("/add")
    public ResponseEntity<Container> addContainer(@RequestBody Container container, HttpServletRequest request) {
        String token=request.getHeader("Authorization").substring(7);
        String username=jwtService.extractUsername(token);
        Optional<User> user=userRepository.findByUsername(username);
        container.setUser(user.get());
        containerRepository.saveAndFlush(container);
        return ResponseEntity.ok(container);
    }

    @GetMapping("/get-all")
    public List<Container> getAllContainers() {
        return containerRepository.findAll();
    }
}
