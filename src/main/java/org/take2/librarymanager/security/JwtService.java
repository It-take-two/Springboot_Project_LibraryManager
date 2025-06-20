package org.take2.librarymanager.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    private Key secretKey;

    public final long accessTokenValidityMs = 15L * 60 * 1000;
    public final long refreshTokenValidityMs = 30L * 24 * 60 * 60 * 1000;

    @PostConstruct
    private void init() {
        this.secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));
    }

    private String generateToken(Long userId, String type, long expiry) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiry);
        return Jwts.builder()
                .subject(userId.toString())
                .claim("type", type)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims parseAllClaims(String token) {
        String rawToken = token.startsWith("Bearer ") ? token.substring(7) : token;
        try {
            JwtParser parser = Jwts.parser()
                    .setSigningKey(secretKey)
                    .build();
            return parser.parseSignedClaims(rawToken).getPayload();
        } catch (Exception e) {
            return null;
        }
    }

    public String generateAccessToken(Long userId) {
        return generateToken(userId, "access", accessTokenValidityMs);
    }

    public String generateRefreshToken(Long userId) {
        return generateToken(userId, "refresh", refreshTokenValidityMs);
    }

    public boolean validateAccessToken(String accessToken) {
        Claims claims = parseAllClaims(accessToken);
        if (claims == null) return false;
        Object tokenType = claims.get("type");
        return "access".equals(tokenType);
    }

    public boolean validateRefreshToken(String refreshToken) {
        Claims claims = parseAllClaims(refreshToken);
        if (claims == null) return false;
        Object tokenType = claims.get("type");
        return "refresh".equals(tokenType);
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = parseAllClaims(token);
        if (claims == null) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(401), "Invalid token");
        }
        return Long.parseLong(claims.getSubject());
    }
}