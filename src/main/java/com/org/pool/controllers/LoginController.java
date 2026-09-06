package com.org.pool.controllers;

import com.org.pool.domain.dtos.LoginRequestDto;
import com.org.pool.domain.dtos.LoginResponseDto;
import com.org.pool.services.LoginService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/login")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping
    public ResponseEntity<LoginResponseDto> login(
            @Valid @RequestBody LoginRequestDto loginRequestDto
            ) {
        LoginResponseDto loginResponseDto = loginService.login(loginRequestDto);
        return ResponseEntity.ok(loginResponseDto);
    }


}
