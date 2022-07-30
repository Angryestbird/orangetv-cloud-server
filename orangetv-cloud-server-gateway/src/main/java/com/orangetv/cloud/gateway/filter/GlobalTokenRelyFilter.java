package com.orangetv.cloud.gateway.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * can also config Default Filters via YML
 */
@Slf4j
@RequiredArgsConstructor
public class GlobalTokenRelyFilter implements GlobalFilter, Ordered {
    private final GatewayFilter gatewayFilter;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("applying GlobalTokenRelyFilter...");
        return gatewayFilter.filter(exchange, chain);
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
