package CodingChallenge.BlogAPI.service;

import CodingChallenge.BlogAPI.security.auth.request.AuthRequest;
import CodingChallenge.BlogAPI.security.auth.request.RegisterRequest;
import CodingChallenge.BlogAPI.security.auth.response.AuthResponse;

public interface UserService {
    AuthResponse register(RegisterRequest request);
    AuthResponse authenticate(AuthRequest request);
}
