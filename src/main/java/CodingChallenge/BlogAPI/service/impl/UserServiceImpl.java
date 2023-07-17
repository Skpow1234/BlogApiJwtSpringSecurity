package CodingChallenge.BlogAPI.service.impl;

import CodingChallenge.BlogAPI.domain.User;
import CodingChallenge.BlogAPI.domain.UserRole;
import CodingChallenge.BlogAPI.repository.UserRepository;
import CodingChallenge.BlogAPI.security.auth.request.AuthRequest;
import CodingChallenge.BlogAPI.security.auth.request.RegisterRequest;
import CodingChallenge.BlogAPI.security.auth.response.AuthResponse;
import CodingChallenge.BlogAPI.security.jwt.JwtService;
import CodingChallenge.BlogAPI.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    @Override
    public AuthResponse register(RegisterRequest request) {
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .userRole(UserRole.USER)
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);

        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }

    @Override
    public AuthResponse authenticate(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()

                )
        );
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(()->
                        new UsernameNotFoundException("User not found."));
        var jwtToken = jwtService.generateToken(user);
        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }
}

