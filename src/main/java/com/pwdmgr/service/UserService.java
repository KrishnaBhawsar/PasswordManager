package com.pwdmgr.service;

import com.pwdmgr.model.User;
import com.pwdmgr.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void doSignup(User user) throws DataIntegrityViolationException {
        User newUser=User.builder()
                        .username(user.getUsername())
                        .password(passwordEncoder.encode(user.getPassword()))
                        .build();
        userRepository.saveAndFlush(newUser);
    }
}
