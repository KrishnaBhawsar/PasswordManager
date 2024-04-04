package com.pwdmgr.controller;

import com.pwdmgr.model.Account;
import com.pwdmgr.model.Container;
import com.pwdmgr.model.User;
import com.pwdmgr.repository.AccountRepository;
import com.pwdmgr.repository.ContainerRepository;
import com.pwdmgr.repository.UserRepository;
import com.pwdmgr.security.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountRepository accountRepository;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final ContainerRepository containerRepository;

    @PostMapping("/add-account")
    public ResponseEntity<Account> addAccount(@RequestBody Account account,
                                              @RequestParam("containerId") Integer containerID,
                                              HttpServletRequest request) {
        Optional<Container> container=containerRepository.findById(containerID);
        account.setContainer(container.get());
        accountRepository.saveAndFlush(account);
        return ResponseEntity.ok(account);
    }
}
