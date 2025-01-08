package faang.school.analytics.listenerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import faang.school.analytics.AnalyticsServiceApp;
import faang.school.analytics.config.redis.RedisConfig;
import faang.school.analytics.dto.RecommendationEvent;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = {AnalyticsServiceApp.class, RedisConfig.class})
@Testcontainers
class RecommendationEventListenerIntegrationTest {

    private static final GenericContainer<?> redisContainer = new GenericContainer<>("redis:7.0.0").withExposedPorts(6379);

    @DynamicPropertySource
    static void redisProperties(org.springframework.test.context.DynamicPropertyRegistry registry) {
        redisContainer.start();
        registry.add("spring.data.redis.host", redisContainer::getHost);
        registry.add("spring.data.redis.port", () -> redisContainer.getMappedPort(6379));
    }

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @MockBean
    private AnalyticsEventService analyticsEventService;

    @Test
    void testRecommendationEventListenerProcessesMessages() {

        RecommendationEvent event = new RecommendationEvent(1L, 2L, 3L, LocalDateTime.now());
        redisTemplate.convertAndSend("recommendation_event_topic", event);


        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
            verify(analyticsEventService, times(1)).processRecommendationEvent(event);
        });
    }
}

