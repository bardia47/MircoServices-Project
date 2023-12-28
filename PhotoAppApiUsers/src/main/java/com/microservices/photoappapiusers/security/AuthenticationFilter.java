package com.microservices.photoappapiusers.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservices.photoappapiusers.model.LoginRequestModel;
import com.microservices.photoappapiusers.service.UserService;
import com.microservices.photoappapiusers.shared.UserDto;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.bouncycastle.jcajce.provider.digest.SHA512;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.Signature;
import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    UserService userService;

    Long tokenExpireTime;

    String tokenSecret;

    public AuthenticationFilter(AuthenticationManager authenticationManager, UserService userService, Long tokenExpireTime, String tokenSecret) {
        super(authenticationManager);
        this.userService = userService;
        this.tokenExpireTime = tokenExpireTime;
        this.tokenSecret = tokenSecret;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
            throws AuthenticationException {
        try {

            LoginRequestModel creds = new ObjectMapper().readValue(req.getInputStream(), LoginRequestModel.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getPassword(), new ArrayList<>()));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        //   byte[] encoded = Base64.getEncoder().encode(tokenSecret.getBytes());
        //   SecretKey secretKey = new SecretKeySpec(encoded, "SHA512");
        String username = ((User) authResult.getPrincipal()).getUsername();
        UserDto userDto = userService.getUserDetailsByEmail(username);
        String token = Jwts.builder()
                .subject(userDto.getUserId()).expiration(Date.from(Instant.now().plusSeconds(tokenExpireTime)))
                .issuedAt(Date.from(Instant.now())).signWith(
                        SignatureAlgorithm.HS256,
                        tokenSecret.getBytes("UTF-8")
                ).compact();
        response.addHeader("token", token);
        response.addHeader("userId", userDto.getUserId());


    }
}
