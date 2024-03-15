package com.example.bookstore.filter;

import com.example.bookstore.entity.User;
import com.example.bookstore.service.JWTTokenService;
import com.example.bookstore.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Order(1)
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final String AUTH_HEADER = "Authorization";
    private final String PREFIX_TOKEN = "Bearer ";

    private final JWTTokenService jwtTokenService;
    private final UserService userService;

    @Autowired
    public JwtFilter(JWTTokenService jwtTokenService, UserService userService) {
        this.jwtTokenService = jwtTokenService;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        var token = getJWTToken(request);
        if (jwtTokenService.validate(token)) {
            var userId = jwtTokenService.getSubject(token);
            User user = userService.getUserById(UUID.fromString(userId));
            var authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(),
                    user.getPassword(), List.of(new SimpleGrantedAuthority("USER")));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } else {
            var authenticationToken = new UsernamePasswordAuthenticationToken(null, null,
                    List.of(new SimpleGrantedAuthority("UNAUTHENTICATED")));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        filterChain.doFilter(request, response);
    }

    private String getJWTToken(HttpServletRequest request) {
        String authenticationHeader = request.getHeader(AUTH_HEADER);
        if (authenticationHeader == null || !authenticationHeader.startsWith(PREFIX_TOKEN))
            return null;
        String token = authenticationHeader.replace(PREFIX_TOKEN, "");
        System.out.println("JWT Token: " + token);
        return token;
    }
}