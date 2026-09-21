package com.carve.cravex.jwtsecurity;

import com.carve.cravex.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTutil {

    private final SecretKey secretKey;

    public JWTutil(@Value("${jwt.signature}") String jwtSignature) {
        this.secretKey = Keys.hmacShaKeyFor(jwtSignature.getBytes(StandardCharsets.UTF_8));
    }
    public String createJwtToken(User user){
        return Jwts.builder()
                .subject(String.valueOf(user.getId()))
                .claim("role",user.getRole().name())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+3600*1000))
                .signWith(secretKey)
                .compact();
    }
    public Claims parseClaims(String token){
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    public Date extractExpiration(String token) {
        return parseClaims(token).getExpiration();
    }
}
