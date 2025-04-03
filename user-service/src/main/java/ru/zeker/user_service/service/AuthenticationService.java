package ru.zeker.user_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zeker.user_service.domain.dto.AuthenticationResponse;
import ru.zeker.user_service.domain.dto.LoginRequest;
import ru.zeker.user_service.domain.dto.RegisterRequest;
import ru.zeker.user_service.domain.model.RefreshToken;
import ru.zeker.user_service.domain.model.Role;
import ru.zeker.user_service.domain.model.User;
import ru.zeker.user_service.exception.UserAlreadyExistsException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;

    public AuthenticationResponse register(RegisterRequest request){
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .role(Role.USER)
                .build();
        userService.create(user);
        String jwtToken = jwtService.generateToken(user);
        String refreshToken = refreshTokenService.createRefreshToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponse login(LoginRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        User user = userService.findByEmail(request.getEmail());
        String jwtToken = jwtService.generateToken(user);
        refreshTokenService.deleteByUserId(user.getId());
        String refreshToken = refreshTokenService.createRefreshToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponse refreshToken(String refreshToken) {
        RefreshToken token = refreshTokenService.verifyRefreshToken(refreshToken);
        User user = token.getUser();
        String jwtToken = jwtService.generateToken(user);
        String newRefreshToken = refreshTokenService.rotateRefreshToken(token);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .refreshToken(newRefreshToken)
                .build();
    }
}
