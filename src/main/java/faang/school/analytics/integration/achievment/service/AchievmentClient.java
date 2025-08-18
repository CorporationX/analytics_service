package faang.school.analytics.integration.achievment.service;

import faang.school.analytics.integration.achievment.config.AchievmentClientProperties;
import faang.school.analytics.integration.achievment.dto.AchievmentResponseDto;
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
@EnableConfigurationProperties(AchievmentClientProperties.class)
@RequiredArgsConstructor
@Slf4j
public class AchievmentClient implements AchievmentServiceClient {
    private final AchievmentClientProperties properties;

    @Override
    public AchievmentResponseDto getAchievment(long id) {
        ResponseEntity<AchievmentResponseDto> responseEntity = WebClient.builder()
                .baseUrl("http://" + properties.host() + ":" + properties.port()).build()
                .get()
                .uri(u -> {
                    return u.path(properties.getAchievmentUrl() + "/" + id)
                            .build();
                })
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(AchievmentResponseDto.class)
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

