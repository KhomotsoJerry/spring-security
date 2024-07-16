package com.security.Spring_Security.configAuth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtApplication extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String userEmail;
        final String jwt;
        if (authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }
        // extracting my jwt token
        jwt = authHeader.substring(7);
        // extracting username from jwt service class
        userEmail = jwtService.extractUsername(jwt);
        // check if userName is empty and verified
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication()==null){
             UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
             if (jwtService.isTokenValid(jwt,userDetails)){
                 UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                           userDetails,
                         null,
                         userDetails.getAuthorities()
                 );
                 auth.setDetails(
                         new WebAuthenticationDetailsSource().buildDetails(request)
                 );
                 SecurityContextHolder.getContext().setAuthentication(auth);
             }
        }
        filterChain.doFilter(request,response);
    }
}
