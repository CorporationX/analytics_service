package faang.school.analytics.integration.post.service;

import faang.school.analytics.integration.post.config.PostClientProperties;
import faang.school.analytics.integration.post.dto.PostResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.net.URISyntaxException;

@Service
@EnableConfigurationProperties(PostClientProperties.class)
@RequiredArgsConstructor
@Slf4j
public class PostClient implements PostServiceClient {
    private final PostClientProperties properties;

    @Override
    public PostResponseDto getPost(long id) {
        ResponseEntity<PostResponseDto> responseEntity = WebClient.builder()
                .baseUrl("http://" + properties.host() + ":" + properties.port()).build()
                .get()
                .uri(u -> {
                    return u.path(properties.getPostUrl() + "/" + id)
                            .build();
                })
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(PostResponseDto.class)
                .onErrorMap(e -> {
                    try {
                        return new WebClientRequestException(e, HttpMethod.GET, new URI(""), HttpHeaders.EMPTY);
                    } catch (URISyntaxException ex) {
                        throw new RuntimeException(ex);
                    }
                })
                .onErrorMap(e -> new WebClientResponseException(e.getMessage(),
                                HttpStatusCode.valueOf(426).value(),
                                "Произошла ошибка при обработке ответа",
                                null, null, null
                        )
                )
                .block();
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return responseEntity.getBody();
        } else {
            throw new ResponseStatusException(responseEntity.getStatusCode(), responseEntity.getBody().toString());
        }
    }
}
