package com.pwdmgr.controller;

import com.pwdmgr.model.Account;
import com.pwdmgr.model.Container;
import com.pwdmgr.repository.AccountRepository;
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
@RequestMapping("/account")
public class AccountController {

    private final AccountRepository accountRepository;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final ContainerRepository containerRepository;

    @PostMapping("/add")
    public ResponseEntity<Account> addAccount(@RequestBody Account account,
                                              HttpServletRequest request) {
        Optional<Container> container=containerRepository.findById(account.getContainerId());
        account.setContainer(container.get());
        accountRepository.saveAndFlush(account);
        return ResponseEntity.ok(account);
    }

    @GetMapping("/remove")
    public ResponseEntity<String> removeAccount(@RequestParam("accountId") Integer accountId) {
        try{
            accountRepository.deleteById(accountId);
        } catch (Exception e) {
            return ResponseEntity.ok("Unable to delete account entity");
        }
        return ResponseEntity.ok("successfully deleted account entity");
    }

    @PutMapping("/edit")
    public ResponseEntity<Account> edit(@RequestBody Account account) {
        Container container=accountRepository.findById(account.getAccountId()).get().getContainer();
        accountRepository.deleteById(account.getAccountId());
        Account newAccount=Account.builder()
                .email(account.getEmail())
                .password(account.getPassword())
                .container(container).build();
        accountRepository.saveAndFlush(newAccount);
        return ResponseEntity.ok(newAccount);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Account> delete(@RequestParam("accountId") Integer accountId) {
        Optional<Account> account=accountRepository.findById(accountId);
        accountRepository.deleteById(accountId);
        return ResponseEntity.ok(account.get());
    }

    @GetMapping("/get-by-id")
    public ResponseEntity<Account> getById(@RequestParam("id") Integer id) {
        return ResponseEntity.ok(accountRepository.findById(id).get());
    }
}
