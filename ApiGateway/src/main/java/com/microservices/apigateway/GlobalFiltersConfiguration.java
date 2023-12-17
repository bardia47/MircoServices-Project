package com.microservices.apigateway;

import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import reactor.core.publisher.Mono;

@Configuration
@Log4j2
public class GlobalFiltersConfiguration {
    @Order(2)

    @Bean
    public GlobalFilter secondPreFilter() {
        log.info("my second pre Filter executed");
        return ((exchange, chain) -> {
            return chain.filter(exchange).then(Mono.fromRunnable(() ->
                    log.info("my second post filter executed")));
        });
    }
}
