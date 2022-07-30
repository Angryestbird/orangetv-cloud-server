package com.orangetv.cloud.gateway.filter;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Builder
@RequiredArgsConstructor
public class RewriteLocationFilter implements WebFilter {

    @Getter
    private final String regexp;
    @Getter
    private final String replacement;

    @Override
    @NonNull
    public Mono<Void> filter(@NonNull ServerWebExchange exchange, WebFilterChain chain) {
        return chain.filter(exchange).then(Mono.fromRunnable(
                () -> rewriteHeaders(exchange)));
    }

    void rewriteHeaders(ServerWebExchange exchange) {
        if (exchange.getResponse().getStatusCode() == HttpStatus.FOUND) {
            final HttpHeaders responseHeaders = exchange.getResponse().getHeaders();
            responseHeaders.computeIfPresent("Location", (k, v) -> rewriteHeaders(v));
        }
    }

    List<String> rewriteHeaders(List<String> headers) {
        ArrayList<String> rewrittenHeaders = new ArrayList<>();
        for (String header : headers) {
            String rewriten = rewrite(header, getRegexp(), getReplacement());
            rewrittenHeaders.add(rewriten);
        }
        return rewrittenHeaders;
    }

    static String rewrite(String value, String regexp, String replacement) {
        return value.replaceAll(regexp, replacement);
    }
}
