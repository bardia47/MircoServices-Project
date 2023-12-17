package com.microservices.apigateway;

import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Set;

@Component
@Log4j2
public class MyPreFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            String requestPath = exchange.getRequest().getPath().toString();
            log.info("request path: " + requestPath);
            HttpHeaders headers = exchange.getRequest().getHeaders();

            Set<String> headerNames = headers.keySet();
            headerNames.forEach((headerName) ->
            {
                String value = headers.getFirst(headerName);
                log.info(headerName + " " + value);
            });
        }));
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
