package com.habit.thehabit.config;

import com.habit.thehabit.config.jwt.JwtAccessDeniedHandler;
import com.habit.thehabit.config.jwt.JwtAuthenticationEntryPoint;
import com.habit.thehabit.config.jwt.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;


@Configuration
@EnableWebSecurity
@EnableJpaRepositories(basePackages = "com.habit.thehabit")
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Autowired
    public ApplicationSecurityConfig(TokenProvider tokenProvider, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
                                     JwtAccessDeniedHandler jwtAccessDeniedHandler){

        this.tokenProvider = tokenProvider;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                /** CSRF disable */
                .csrf().disable()

                /** exception handling */
                .exceptionHandling()
                /** ?????? ????????? ?????? ????????? ??? ???????????? exception ????????? ?????? entry point ?????? */
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                /** ???????????? ?????? ?????? ????????? ???????????? exception ?????? */
                .accessDeniedHandler(jwtAccessDeniedHandler)

                /** ????????? ???????????? ?????? ?????????, Stateless??? ?????? */
                .and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                    .authorizeRequests()
                    .antMatchers("/").permitAll()
                    .antMatchers("/v1/auths/login").permitAll()
                    .antMatchers(HttpMethod.POST,"/v1/members").permitAll()
                    .antMatchers("/v1/**").hasAnyAuthority("USER", "ADMIN")
                    
                    //.antMatchers(HttpMethod.PUT,"/v1/members").hasAuthority("USER")
                    //.antMatchers(HttpMethod.POST, "/v1/records").hasAuthority("USER")
                    //.antMatchers(HttpMethod.PATCH, "/v1/records").hasAuthority("USER")
                    //.antMatchers(HttpMethod.DELETE, "/v1/records").hasAuthority("USER")
                    //.antMatchers(HttpMethod.GET,"/v1/clubs").hasAuthority("USER")
                    //.antMatchers(HttpMethod.POST,"/v1/clubs").hasAuthority("USER")
                    //.antMatchers(HttpMethod.PUT,"/v1/clubs").hasAuthority("USER")
                    //.antMatchers(HttpMethod.GET,"/v1/attendance").hasAuthority("USER")
                    //.antMatchers(HttpMethod.POST,"/v1/attendance").hasAuthority("USER")
                    //.antMatchers(HttpMethod.PUT,"/v1/attendance").hasAuthority("USER")

                .and()
                    .cors()

                .and()
                    .logout()
                        .logoutUrl("/v1/auths/logout")
                        .logoutSuccessUrl("/v1/records/user")
                .and()
                /** JWT ?????? ????????? ????????? jwt filter??? ?????? */
                    .apply(new JwtSecurityConfig(tokenProvider));
    }


    /*????????? ??????*/
    /** cors ?????? ?????? ????????? ????????????. */
    @Bean
    CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();

        /** ????????? ?????? ????????? cors ????????? ??????(????????? unity??? ???????????? ??????) */
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "PUT", "POST", "DELETE", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("Access-Control-Allow-Origin", "Content-Type", "Access-Control-Allow-Headers", "Authorization", "X-Requested-With"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
