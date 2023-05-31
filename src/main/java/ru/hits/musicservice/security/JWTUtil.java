package ru.hits.musicservice.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;

@Component
public class JWTUtil {

    @Value("${jwt-token.secret}")
    private String secret;

    @Value("${jwt-token.issuer}")
    private String issuer;

    @Value("${jwt-token.subject}")
    private String subject;

    public String generateToken(UUID id) {
        Date expirationDate = Date.from(ZonedDateTime.now().plusMinutes(60).toInstant());
        return JWT.create()
                .withSubject(subject)
                .withClaim("id", id.toString())
                .withIssuedAt(new Date())
                .withIssuer(issuer)
                .withExpiresAt(expirationDate)
                .sign(Algorithm.HMAC256(secret));
    }

    public UUID validateTokenAndRetrieveClaim(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject(subject)
                .withIssuer(issuer)
                .build();
        DecodedJWT decodedJWT = verifier.verify(token);
        return UUID.fromString(decodedJWT.getClaim("id").asString());
    }

}
