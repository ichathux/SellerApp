//package com.example.demo.security;
//
//import com.auth0.jwt.JWT;
//import com.auth0.jwt.JWTVerifier;
//import com.auth0.jwt.algorithms.Algorithm;
//import com.auth0.jwt.exceptions.JWTVerificationException;
//import lombok.Data;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.stereotype.Service;
//
//import java.time.Instant;
//import java.util.Date;
//
//@Service
//@Slf4j
//@Data
//public class JwtTokenService {
//    private final Algorithm hmac512;
//    private final JWTVerifier verifier;
//    @Value("${jwt.expiration.time}")
//    private Long jwtExpirationInMillis;
//
//    public JwtTokenService(@Value("${jwt.secret}") final String secret) {
//        this.hmac512 = Algorithm.HMAC512(secret);
//        this.verifier = JWT.require(this.hmac512).build();
//    }
//
//    //    public String generateToken(final UserDetails userDetails) {
////        return JWT.create()
////                .withSubject(userDetails.getUsername())
////                .withExpiresAt(Date.from(Instant.now().plusMillis(jwtExpirationInMillis)))
////                .sign(this.hmac512);
////    }
//    public String generateToken(Authentication authentication) {
//        User principal = (User) authentication.getPrincipal();
//        return generateTokenWithUsername(principal.getUsername());
//    }
//
//    public String generateTokenWithUsername(String username) {
//        return JWT.create()
//                .withSubject(username)
//                .withExpiresAt(Date.from(Instant.now().plusMillis(jwtExpirationInMillis)))
//                .sign(this.hmac512);
//    }
//
//    public String validateTokenAndGetUsername(final String token) {
//        try {
//            return verifier.verify(token).getSubject();
//        } catch (final JWTVerificationException verificationEx) {
//            log.warn("token invalid: {}", verificationEx.getMessage());
//            return null;
//        }
//    }
//
//}
