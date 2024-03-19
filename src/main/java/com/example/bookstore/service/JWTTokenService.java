package com.example.bookstore.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.bookstore.dto.auth.ResponseTokenDTO;
import com.example.bookstore.dto.UserDTO;
import com.example.bookstore.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * The `JWTTokenService` class is responsible for generating and validating JWT tokens.
 */

@Service
public class JWTTokenService {
    public static final String CLAIM_ROLE_AUTHORITY = "roleAuthority";
    private final String jwtSecret;
    private final Integer jwtExpirationMs;
    private final ModelMapper modelMapper;

    public JWTTokenService(@Value("${app.jwtSecret}") String jwtSecret,
                           @Value("${app.jwtExpirationMs}") Integer jwtExpirationMs,
                           ModelMapper modelMapper) {
        this.jwtSecret = jwtSecret;
        this.jwtExpirationMs = jwtExpirationMs;
        this.modelMapper = modelMapper;
    }

    // Generating an access token with user ID and email as claims
    private String generateAccessToken(String userId, String userEmail) {
        return JWT.create().withSubject(userId)
                .withClaim("email", userEmail)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date((new Date()).getTime() + jwtExpirationMs))
                .sign(Algorithm.HMAC512(jwtSecret));
    }

    // Validating a JWT token
    public boolean validate(String token) {
        var validation = JWT.require(Algorithm.HMAC512(jwtSecret)).build();
        try {
            validation.verify(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    // Extracting the subject (user ID) from a JWT token
    public String getSubject(String token) {
        return JWT.decode(token).getSubject();
    }

    // Generating tokens (access token) for a user
    public ResponseTokenDTO generateTokens(User user) {
        var accessToken = generateAccessToken(user.getId().toString(), user.getUsername());
        var userDTO = modelMapper.map(user, UserDTO.class);
        return new ResponseTokenDTO(userDTO, accessToken, jwtExpirationMs);
    }
}