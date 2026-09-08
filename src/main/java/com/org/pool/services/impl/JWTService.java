package com.org.pool.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JWTService {

    private final JwtEncoder jwtEncoder;

    public String generateToken(Authentication authentication) {

        List<String> roles = authentication.getAuthorities()
                            .stream()
                            .map(GrantedAuthority::getAuthority)
                            .filter(authority -> authority.startsWith("ROLE_"))
                            .toList();

        Instant now = Instant.now();
        Instant expiry = now.plus(1, ChronoUnit.HOURS);

        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                                    .subject(authentication.getName())
                                    .issuedAt(now)
                                    .expiresAt(expiry)
                                    .claim("roles", roles)
                                    .build();

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();

        JwtEncoderParameters parameters = JwtEncoderParameters.from(header, jwtClaimsSet);

        return jwtEncoder.encode(parameters).getTokenValue();

    }

}
