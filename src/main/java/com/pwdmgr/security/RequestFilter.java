package com.pwdmgr.security;

import com.pwdmgr.security.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Configuration
@RequiredArgsConstructor
public class RequestFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    @Autowired
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String header=request.getHeader("Authorization");
        if(header==null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return ;
        }
        String token=header.substring(7);
        String username=jwtService.extractUsername(token);

        if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null) {
            UserDetails user=userDetailsService.loadUserByUsername(username);
            if(jwtService.isTokenValid(token, user)) {
                UsernamePasswordAuthenticationToken authenticationToken=new
                        UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(request, response);

    }
}
