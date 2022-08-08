package com.orangetv.cloud.gateway;

import com.orangetv.cloud.gateway.config.OrangeCloudTvConfigProps;
import com.orangetv.cloud.gateway.filter.GlobalTokenRelyFilter;
import com.orangetv.cloud.gateway.filter.RewriteLocationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.TokenRelayGatewayFilterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Slf4j
@SpringBootApplication
@ConfigurationPropertiesScan
@RequiredArgsConstructor
public class GatewayApplication {

    private final OrangeCloudTvConfigProps props;

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @Bean
    GlobalFilter globalTokenRelayFilter(TokenRelayGatewayFilterFactory factory) {
        return new GlobalTokenRelyFilter(factory.apply());
    }

    @Bean
    SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) {

        // @formatter:off
        var security = http.authorizeExchange((authorize) -> authorize
            .pathMatchers(HttpMethod.POST, "/AUTH-SERVER/login").permitAll()
            .pathMatchers(HttpMethod.POST, "/actuator/gateway/**")
                .hasAnyAuthority("SCOPE_gateway.metadata.write")
            .pathMatchers(HttpMethod.GET, "/actuator/health")
                .permitAll()
            .anyExchange().authenticated()
        ).csrf().disable().oauth2Login().and();
        // @formatter:on

        if (props.isEnableLocationRewrite()) {
            security.addFilterBefore(RewriteLocationFilter.builder()
                    .regexp("^/(?<segment>.*)$").replacement("/api/${segment}")
                    .build(), SecurityWebFiltersOrder.HTTP_BASIC);
        }

        // enable resource server for actuator
        security.oauth2ResourceServer().jwt();
        return http.build();
    }
}
