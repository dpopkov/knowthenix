package io.dpopkov.knowthenix.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import java.util.Date;

@Service
public class JwtService {

    private static final long EXPIRATION_TIME_MILLIS = 30 * 60 * 1000;
    private RSAPrivateKey privateKey;
    private RSAPublicKey publicKey;

    @PostConstruct
    private void initKeys() throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        KeyPair keyPair = generator.generateKeyPair();
        privateKey = (RSAPrivateKey) keyPair.getPrivate();
        publicKey = (RSAPublicKey) keyPair.getPublic();
    }

    // todo: If a database with real users is used then the user ID might be put in here.
    public String generateToken(String username, String role) {
        return JWT.create()
                .withClaim("user", username)
                .withClaim("role", role)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MILLIS))
                .sign(Algorithm.RSA256(publicKey, privateKey));
    }

    /**
     * Validates the specified token and returns the payload in JSON format if the token is valid
     * or throws exception if the token is not valid.
     * @param token JWT token
     * @return the payload of the token in JSON format
     * @throws JWTVerificationException if verification of this token fails
     */
    public String validateToken(String token) throws JWTVerificationException {
        String base64encoded = JWT.require(Algorithm.RSA256(publicKey, privateKey)).build()
                .verify(token)
                .getPayload();
        return new String(Base64.getDecoder().decode(base64encoded));
    }
}
