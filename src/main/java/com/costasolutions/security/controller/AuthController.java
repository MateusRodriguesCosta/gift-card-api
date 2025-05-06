package com.costasolutions.security.controller;

import com.costasolutions.security.dto.AuthRequest;
import com.costasolutions.security.dto.AuthResponse;
import com.costasolutions.security.dto.RefreshRequest;
import com.costasolutions.security.dto.RegisterRequest;
import com.costasolutions.security.service.TokenService;
import com.costasolutions.security.util.JwtUtil;
import com.costasolutions.giftcards.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final TokenService tokenService;

    public AuthController(AuthenticationManager authManager,
                          JwtUtil jwtUtil,
                          UserService userService,
                          TokenService tokenService) {
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        userService.register(req.username(), req.password(), "ROLE_USER");
        return ResponseEntity.ok(Map.of("message", "User registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest req) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken((req.username()), req.password())
        );
        String accessToken = jwtUtil.generateAccessToken(auth.getName());
        String refreshToken = jwtUtil.generateRefreshToken(auth.getName());
        return ResponseEntity.ok(new AuthResponse(accessToken, refreshToken));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshRequest refreshRequest) {
        String refreshToken = refreshRequest.refreshToken();
        AuthResponse tokens = tokenService.refreshToken(refreshToken);
        return ResponseEntity.ok(tokens);
    }
}
