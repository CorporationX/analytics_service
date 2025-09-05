package faang.school.analytics.listener;

import com.redis.testcontainers.RedisContainer;
import faang.school.analytics.dto.RecommendationEvent;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

/**
 * Интеграционный тест для проверки связи между отправкой ивента рекомендации в базу данных и его
 * корректное сохранения в ней
 *
 * @author Linempy
 * @since 02.09.2025
 */
@Testcontainers
@SpringBootTest
public class RecommendationEventListenerIT {

    @Container
    private static final PostgreSQLContainer<?> POSTGRESQL_CONTAINER =
            new PostgreSQLContainer<>("postgres:13.6");

    @Container
    private static final RedisContainer REDIS_CONTAINER =
            new RedisContainer(DockerImageName.parse("redis/redis-stack:latest"));

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private AnalyticsEventRepository analyticsEventRepository;

    @Autowired
    private RecommendationEventListener eventListener;

    @Autowired
    private MessageListenerTestData testData;

    @DynamicPropertySource
    static void postgresqlProperties(DynamicPropertyRegistry registry) {
        POSTGRESQL_CONTAINER.start();
        REDIS_CONTAINER.start();

        registry.add("spring.datasource.url", POSTGRESQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRESQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRESQL_CONTAINER::getPassword);

        registry.add("spring.data.redis.port", () -> REDIS_CONTAINER.getMappedPort(6379));
        registry.add("spring.data.redis.host", REDIS_CONTAINER::getHost);
    }


    @Test
    public void shouldProcessEventAndSaveToDatabase() {
        RecommendationEvent event = new RecommendationEvent(1L, 2L, 3L, null);
        redisTemplate.convertAndSend(testData.recommendationTopic, event);

        await().atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    AnalyticsEvent savedEvent = analyticsEventRepository.findByIdOrThrow(1L);

                    assertThat(savedEvent).isNotNull();
                    assertThat(savedEvent.getEventType()).isEqualTo(EventType.RECOMMENDATION_RECEIVED);
                    assertThat(savedEvent.getActorId()).isEqualTo(1L);
                });
    }
}