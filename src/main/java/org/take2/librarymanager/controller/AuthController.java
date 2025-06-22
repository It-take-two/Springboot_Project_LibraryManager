package org.take2.librarymanager.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.take2.librarymanager.security.AuthService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public record AuthRequest(
            String name,
            String password
    ) {}

    public record RefreshToken(String refreshToken) {}

    @PostMapping("/register")
    public Object register(@RequestBody AuthRequest authRequest) {
        return authService.register(authRequest.name, authRequest.password);
    }

    @PostMapping("/login")
    public Object login(@RequestBody AuthRequest body) {
        return authService.login(body.name, body.password);
    }

    @PostMapping("/refresh")
    public Object refresh(@RequestBody RefreshToken body) {
        return authService.refresh(body.refreshToken);
    }
}

