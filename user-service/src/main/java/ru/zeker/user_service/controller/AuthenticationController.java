package ru.zeker.user_service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.zeker.user_service.domain.dto.AuthenticationResponse;
import ru.zeker.user_service.domain.dto.LoginRequest;
import ru.zeker.user_service.domain.dto.RegisterRequest;
import ru.zeker.user_service.service.AuthenticationService;
import ru.zeker.user_service.service.RefreshTokenService;

@RestController
@RequestMapping("/api/v1/auth") //TODO: Убрать приписку api/v1, добавив Strip prefix в конфигурацию Gateway
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody @Valid RegisterRequest request){
            return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @Valid LoginRequest request){
        return ResponseEntity.ok(authenticationService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponse> refreshToken(@CookieValue(name = "refresh_token") String refreshToken){
        return ResponseEntity.ok(authenticationService.refreshToken(refreshToken));
    }

    //TODO: Разобраться в куках и понять, надо серверу удалять или клиенту
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@CookieValue(name = "refresh_token") String refreshToken){
        refreshTokenService.revokeRefreshToken(refreshToken);
        return ResponseEntity.noContent().build();
    }
}
