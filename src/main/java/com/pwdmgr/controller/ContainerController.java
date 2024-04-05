package com.pwdmgr.controller;

import com.pwdmgr.model.Account;
import com.pwdmgr.model.Container;
import com.pwdmgr.model.User;
import com.pwdmgr.repository.ContainerRepository;
import com.pwdmgr.repository.UserRepository;
import com.pwdmgr.security.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/container")
public class ContainerController {

    private final ContainerRepository containerRepository;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @PostMapping("/add")
    public ResponseEntity<Container> addContainer(@RequestBody Container container, HttpServletRequest request) {
        System.out.println("inside add container");
        String token=request.getHeader("Authorization").substring(7);
        String username=jwtService.extractUsername(token);
        Optional<User> user=userRepository.findByUsername(username);
        for(Account account:container.getAccounts())
                account.setContainer(container);
        container.setUser(user.get());
        containerRepository.saveAndFlush(container);
        return ResponseEntity.ok(container);
    }

    @PostMapping("/remove")
    public ResponseEntity<String> removeContainer(@RequestParam("containerId") Integer containerId) {
        try {
            containerRepository.deleteById(containerId);
        } catch (Exception e) {
            return ResponseEntity.ok("unable to delete container");
        }
        return ResponseEntity.ok("successfully deleted container");
    }

}
