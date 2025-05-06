package com.costasolutions.security.service;

import com.costasolutions.security.dto.AuthResponse;
import com.costasolutions.security.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.stereotype.Service;

@Service
public class TokenService {
    private final JwtUtil jwtUtil;

    public TokenService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public AuthResponse refreshToken(String token) {
        Jws<Claims> claims = jwtUtil.parseToken(token);
        String username = claims.getBody().getSubject();

        String newAccessToken = jwtUtil.generateAccessToken(username);
        String newRefreshToken = jwtUtil.generateRefreshToken(username);

        return new AuthResponse(newAccessToken, newRefreshToken);
    }
}
