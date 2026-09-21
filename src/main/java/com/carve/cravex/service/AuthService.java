package com.carve.cravex.service;

import com.carve.cravex.dto.LoginRequestDto;
import com.carve.cravex.response.LoginResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {

    LoginResponse loginService(LoginRequestDto loginRequestDto);

    String logoutService(HttpServletRequest httpServletRequest);
}
