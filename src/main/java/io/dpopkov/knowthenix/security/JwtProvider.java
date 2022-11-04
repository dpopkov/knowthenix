package io.dpopkov.knowthenix.security;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtProvider {

    String generateToken(UserDetails userDetails);

    VerificationResult verifyToken(String token);
}
