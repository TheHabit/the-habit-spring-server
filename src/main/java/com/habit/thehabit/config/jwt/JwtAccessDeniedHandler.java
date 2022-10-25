package com.habit.thehabit.config.jwt;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        /** 인가되지 않은 곳에 접근하려고 할 때, 403(Forbiden)으로 처리
         * cf) SC : Status Code */
        response.sendError(HttpServletResponse.SC_FORBIDDEN);
    }
}
