package com.microservices.apigateway;

import io.jsonwebtoken.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Component
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

    AuthorizationHeaderFilter() {
        super(Config.class);
    }

    @Value("${token.secret}")
    String tokenSecret;

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return onError(exchange, "no Authorization Header", HttpStatus.UNAUTHORIZED);
            }
            String authorizationHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            String jwt = authorizationHeader.replace("Bearer ", "");
            if (!isJwtValid(jwt))
                return onError(exchange, "JWT Token is not valid", HttpStatus.UNAUTHORIZED);


            return chain.filter(exchange);
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, String noAuthorizationHeader, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    public static class Config {
    }

    private boolean isJwtValid(String jwt) {
        boolean isValid = true;
        byte[] encoded = Base64.getEncoder().encode(tokenSecret.getBytes());
        String subject = null;
        SecretKey secretKey = new SecretKeySpec(encoded, "SHA512");
        JwtParser parser = Jwts.parser().setSigningKey(secretKey).build();
        try {
            Jwt<Header, Claims> parsedToken = (Jwt<Header, Claims>) parser.parse(jwt);
            subject = parsedToken.getBody().getSubject();
        } catch (Exception e) {
            isValid = false;
        }
        if (StringUtils.isEmpty(subject))
            isValid = false;
        return isValid;


    }
}
