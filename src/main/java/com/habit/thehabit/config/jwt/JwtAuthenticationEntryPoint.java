package com.habit.thehabit.config.jwt;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        /** authentication entry point
         * : 인증 과정에서 exception이 발생했을 때 처리 방식 설정
         * 401(unautorized)로 처리 */
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);

    }
}
