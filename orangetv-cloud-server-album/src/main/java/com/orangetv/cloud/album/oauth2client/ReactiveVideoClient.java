package com.orangetv.cloud.album.oauth2client;

import com.orangetv.cloud.album.model.Video;
import com.orangetv.cloud.album.util.Response;
import com.orangetv.cloud.album.util.Template;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId;

@Service
@RequiredArgsConstructor
public class ReactiveVideoClient {

    private final WebClient webClient;

    public Response<Video> updateById(Video video) {
        String uri = Template.render("http://VIDEO-STORE/video/{}", video.getId());
        return webClient.put().uri(uri)
                .attributes(clientRegistrationId("orangetv-cloud-server-album"))
                .bodyValue(video).retrieve()
                .bodyToMono(new ParameterizedTypeReference<Response<Video>>() {
                }).block();
    }

    public String getVideoRepoUrl(Video video) {
        String uri = Template.render("http://VIDEO-STORE/video/repo/{}", video.getRepoId());
        return webClient.get().uri(uri)
                .attributes(clientRegistrationId("orangetv-cloud-server-album"))
                .retrieve().bodyToMono(String.class).block();
    }

    public String ping() {
        return webClient.get().uri("http://VIDEO-STORE/video/test/ping")
                .attributes(clientRegistrationId("orangetv-cloud-server-album"))
                .retrieve().bodyToMono(String.class).block();
    }
}
