package com.carve.cravex.serviceimpl;

import com.carve.cravex.dto.LoginRequestDto;
import com.carve.cravex.response.LoginResponse;
import com.carve.cravex.entity.User;
import com.carve.cravex.jwtsecurity.JWTutil;
import com.carve.cravex.repository.UserRepository;
import com.carve.cravex.service.AuthService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final JWTutil jwTutil;

    private final UserRepository userRepo;

    private final AuthenticationManager authenticationManager;

    private final RedisTemplate<String,String> redisTemplate;

    @Override
    public LoginResponse loginService(LoginRequestDto loginRequestDto) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=
                new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(),loginRequestDto.getPassword());
        authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        User user=userRepo.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String token=jwTutil.createJwtToken(user);
        log.info("Token Created ");
        return LoginResponse.builder()
                .token(token)
                .username(user.getEmail())
                .role(user.getRole().name())
                .userId(user.getId())
                .build();
    }

    @Override
    public String logoutService(HttpServletRequest httpServletRequest) {
        String authHeader=httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if(authHeader==null || !authHeader.startsWith("Bearer ")) throw new RuntimeException("Token missing");
        log.info("Token with Bearer {}",authHeader);
        String jwt=authHeader.substring(7);

        if(redisTemplate.hasKey("BLACKLIST:"+jwt)) return "Already logged out";

        try{
            Date expiry=jwTutil.extractExpiration(jwt);
            long remainingTime=expiry.getTime()-System.currentTimeMillis();

            if(remainingTime<=0) return "Token already expired";

            redisTemplate.opsForValue().set(
                    "BLACKLIST:"+jwt,"logged_out",remainingTime,TimeUnit.MILLISECONDS
            );
            return "You have logged out successfully";
        } catch (ExpiredJwtException e) {
            return "Token already expired";

        } catch (JwtException e) {
            throw new RuntimeException("Invalid token");
        }
    }

}
