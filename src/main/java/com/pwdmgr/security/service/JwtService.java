package com.pwdmgr.security.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {
    private String SECRET_KEY = "0df6e92aa21bf8063f3d91c74e54f376aea95c0327ce6680bd11112dbe1ba2a1";
    private long EXPIRATION_TIME = 24 * 60 * 60 * 1000;

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }
    public Date extractIssuedTime(String token) {
        return extractClaims(token).getIssuedAt();
    }

    public Boolean isTokenValid(String token, UserDetails empDetails) {
        String userEmail=empDetails.getUsername();
        return userEmail.equals(extractUsername(token))
                && !isTokenExpired(token);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails empDetails) {

        Date currentDate=new Date();
        Date expriationDate=new Date(System.currentTimeMillis()+ EXPIRATION_TIME);

        String username=empDetails.getUsername();
        return Jwts.
                builder().
                setClaims(extraClaims).
                setSubject(username).
                setIssuedAt(currentDate).
                setExpiration(expriationDate).
                signWith(getSecretKey()).
                compact();
    }

    private Claims extractClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private SecretKey getSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Boolean isTokenExpired(String token) {
        return extractClaims(token).
                getExpiration().
                before(new Date(System.currentTimeMillis()));
    }
}
