package com.microservices.photoappapiusers.security;

import com.microservices.photoappapiusers.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.AntPathMatcher;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurity {
    @Value("${gateway.ip}")
    List<String> gateWayIp;


    @Value("${token.expire.time}")
    Long tokenExpireTime;

    @Value("${token.secret}")
    String tokenSecret;


    UserService userService;

    public WebSecurity(@Lazy UserService userService) {

        this.userService = userService;
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();
        http.csrf(csrf -> csrf.disable());
      /*  http.authorizeHttpRequests(auth -> {
            auth.requestMatchers(antMatcher(HttpMethod.POST, "/users/"))//.permitAll()
                    .access(new WebExpressionAuthorizationManager(gateWayIp.stream().map(ip -> "hasIpAddress('" + ip + "')")
                            .collect(Collectors.joining(" or "))));
            auth.requestMatchers(antMatcher("/h2-console/**")).permitAll();
            auth.anyRequest().authenticated();

        });*/
        http.addFilter(new AuthenticationFilter(authenticationManager, userService, tokenExpireTime, tokenSecret));
        http.authenticationManager(authenticationManager);
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
