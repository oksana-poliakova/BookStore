package com.example.bookstore.service;

import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import java.security.PublicKey;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.function.Function;


/**
 * @author oksanapoliakova on 15.03.2024
 * @projectName BookStore
 */
@Service
public class JWTService {

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String generateToken(UserDetails userDetails) {
        var issuedAtDate = LocalDateTime.now().toInstant(ZoneOffset.UTC);
        var expirationDate = issuedAtDate.plus(1, ChronoUnit.DAYS);

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(Date.from(issuedAtDate))
                .expiration(Date.from(expirationDate))
                .signWith(getSignInKey())
                .compact();
    }

    public <T> T extractClaim(String token, Function<Claims, T> function) {
        var claims = extractAllClaims(token);
        return function.apply(claims);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        var usernameFormToken = extractUserName(token);
        return usernameFormToken.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public SecretKey getSignInKey() {
        return Keys.hmacShaKeyFor("EG9UhnsZ+veiD/qNenH4qXcj+5mKOBNyYKwZ2f3P/gd2F5QcFM3+jZULVlTvyexWmKhKUJ2W2CDC7fZQe0I0GA==".getBytes());
    }
}
