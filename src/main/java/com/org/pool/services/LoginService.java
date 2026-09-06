package com.org.pool.services;

import com.org.pool.domain.dtos.LoginRequestDto;
import com.org.pool.domain.dtos.LoginResponseDto;

public interface LoginService {
    LoginResponseDto login(LoginRequestDto loginRequest);
}
