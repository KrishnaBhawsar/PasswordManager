package com.pwdmgr.security.entity;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class LoginRequest {
    private String username;
    private String password;
}
