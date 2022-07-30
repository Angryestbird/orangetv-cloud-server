package com.orangetv.cloud.videostore.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orangetv.cloud.videostore.config.OrangeTVConfigProps;
import com.orangetv.cloud.videostore.vo.gateway.RouteDefinition;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId;

@Service
@RequiredArgsConstructor
@Slf4j
public class GatewayClient implements ApplicationRunner {

    private final WebClient webClient;
    private final OrangeTVConfigProps props;
    private final ObjectMapper objectMapper;

    @Override
    @SneakyThrows
    public void run(ApplicationArguments args) {
        while (true) {
            try {
                tryAddRoute();
                break;
            } catch (Exception e) {
                log.warn("failed to add route, retry 30s later", e);
                //noinspection BusyWait
                Thread.sleep(TimeUnit.SECONDS.toMillis(30));
            }
        }
    }

    void tryAddRoute() {
        String videoRepoRouteId = String.format("video-repo-%s", props.getVideoRepoId());
        RouteDefinition gatewayRoute = getGatewayRoute(videoRepoRouteId);
        if (gatewayRoute != null) {
            log.warn("route exists, try add after delete, route: {}", toJsonStr(gatewayRoute));
            delGatewayRoute(videoRepoRouteId);
        }
        log.info("adding route {} to gateway", videoRepoRouteId);
        doAddRoute(videoRepoRouteId);
    }

    void doAddRoute(String videoRepoRouteId) {
        var routeDefinition = new RouteDefinition();
        routeDefinition.setId(videoRepoRouteId);
        routeDefinition.setPredicates(Collections.singletonList(
                new RouteDefinition.PredicateDefinition("Path=/video-repo/" + props.getVideoRepoId() + "/**")));
        routeDefinition.setFilters(Collections.singletonList(
                new RouteDefinition.FilterDefinition("StripPrefix=2")));
        routeDefinition.setUri(props.getVideoRepoUrl());
        addGatewayRoute(routeDefinition);
    }

    public RouteDefinition getGatewayRoute(String routeId) {
        return webClient.get().uri("http://GATEWAY/actuator/gateway/routes/" + routeId)
                .attributes(clientRegistrationId("orangetv-cloud-server-video")).retrieve()
                .bodyToMono(RouteDefinition.class).onErrorResume(WebClientResponseException.class,
                        ex -> ex.getRawStatusCode() == 404 ? Mono.empty() : Mono.error(ex))
                .block();
    }

    public void addGatewayRoute(RouteDefinition routeDefinition) {
        webClient.post().uri("http://GATEWAY/actuator/gateway/routes/" + routeDefinition.getId())
                .bodyValue(routeDefinition).attributes(clientRegistrationId("orangetv-cloud-server-video")).retrieve()
                .bodyToMono(Void.class).block();
    }

    public void delGatewayRoute(String routeId) {
        webClient.delete().uri("http://GATEWAY/actuator/gateway/routes/" + routeId)
                .attributes(clientRegistrationId("orangetv-cloud-server-video")).retrieve()
                .bodyToMono(Void.class).block();
    }

    public ResponseEntity<String> hello() {
        return webClient.get().uri("http://GATEWAY/hello")
                .attributes(clientRegistrationId("orangetv-cloud-server-video")).retrieve()
                .toEntity(String.class).block();
    }

    @SneakyThrows
    private String toJsonStr(Object obj) {
        return objectMapper.writeValueAsString(obj);
    }
}
