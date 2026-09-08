package com.gabriellabritz.build_finance_api.infra.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.gabriellabritz.build_finance_api.domain.user.User;
import com.gabriellabritz.build_finance_api.infra.exceptions.infra.JwtGenerateException;
import com.gabriellabritz.build_finance_api.infra.exceptions.infra.JwtVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class JwtService {
    @Value("${app.security.jwt.secret}")
    private String jwtSecret;

    @Value("${app.security.jwt.access_token_expiration}")
    private Long jwtAccessTokenExpiration;

    @Value("${app.security.jwt.issuer}")
    private String jwtIssuer;

    public String generateJwtToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
            return JWT.create()
                    .withIssuer(jwtIssuer)
                    .withSubject(user.getId().toString())
                    .withExpiresAt(expiration(jwtAccessTokenExpiration))
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            throw new JwtGenerateException("Erro ao gerar token JWT.");
        }
    }

    public String getSubjectFromToken(String token) {
        DecodedJWT decodedJWT;

        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret);

            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(jwtIssuer)
                    .build();

            decodedJWT = verifier.verify(token);

            return decodedJWT.getSubject();
        } catch (JWTVerificationException exception){
            throw new JwtVerificationException("JWT inválido.");
        }
    }

    private Instant expiration(Long seconds) {
        return Instant.now().plusSeconds(seconds);
    }
}
