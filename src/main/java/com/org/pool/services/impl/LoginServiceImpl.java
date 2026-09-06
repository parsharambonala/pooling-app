package com.org.pool.services.impl;

import com.org.pool.domain.dtos.LoginRequestDto;
import com.org.pool.domain.dtos.LoginResponseDto;
import com.org.pool.services.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final AuthenticationManager authenticationManager;

    @Override
    public LoginResponseDto login(LoginRequestDto loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getMailId(),
                        loginRequest.getPassword()
                )
        );

        System.out.println(authentication.getName());

        return new LoginResponseDto("AUTHENTICATED");

    }
}
