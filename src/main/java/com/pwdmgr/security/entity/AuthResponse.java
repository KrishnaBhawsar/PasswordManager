package com.pwdmgr.security.entity;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class AuthResponse {
    private String token;
    private String email;
    private String message;
}
