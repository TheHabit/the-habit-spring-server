package com.habit.thehabit.config.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.habit.thehabit.common.command.app.exception.dto.ApiExceptionDTO;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer";
    private final TokenProvider tokenProvider;

    public JwtFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            String jwt = resolveToken(request);
            manageContextHolder(jwt);
            // dofilter원래 자리

        } catch (RuntimeException e) {
            System.out.println("e = " + e);
            manageApiException(response, e);
        }

        filterChain.doFilter(request, response);
    }

    /** 토큰이 유효할 때, authentication을 context holder에 담아주는 메서드*/
    private void manageContextHolder(String jwt){

        if(StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)){

            System.out.println("step 1");
            /** token에서 authentication을 얻어와서 security context holder에 저장 */
            Authentication authentication = tokenProvider.getAuthentication(jwt);

            System.out.println(authentication);
            System.out.println("step 2");
            /** SecurityContextHolder는 인증된 유저의 정보를 저장. */
            System.out.println("before : SecurityContextHolder.getContext() = " + SecurityContextHolder.getContext());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            System.out.println("after : SecurityContextHolder.getContext() = " + SecurityContextHolder.getContext());
        }

    }

    /** 권한이 없다는 것을 exception을 통해 알리기 위한 메서드 */
    private void manageApiException(HttpServletResponse response, RuntimeException e) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ApiExceptionDTO errorResponse = new ApiExceptionDTO(HttpStatus.UNAUTHORIZED, e.getMessage());

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write(convertObjectToJson(errorResponse));
    }

    /** Object -> Json 변환을 위한 메서드 */
    public String convertObjectToJson(Object object) throws JsonProcessingException {
        if(object == null){
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }

    /** request 헤더로부터 토큰을 꺼내오는 메서드 */
    private String resolveToken(HttpServletRequest request) {

        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        /** bearerToken 안에 text가 있는지 확인 */
        if(StringUtils.hasText(bearerToken)){
            return bearerToken.substring(7);
        }
        return null;
    }
}
