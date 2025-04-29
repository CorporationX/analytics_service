package faang.school.analytics.listener;

import com.redis.testcontainers.RedisContainer;
import faang.school.analytics.config.redis.RedisConfig;
import faang.school.analytics.dto.FundRaisedEvent;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.apache.commons.collections4.IterableUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.LocalDate;
import java.util.List;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@Import(RedisConfig.class)
public class FundRaisedEventListenerIT {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private AnalyticsEventRepository analyticsEventRepository;

    @Test
    public void testPositiveFundRaisedEventListener() {
        FundRaisedEvent event = FundRaisedEvent.builder()
                .userId(1L)
                .raiseDate(LocalDate.now())
                .projectId(2L)
                .build();
        redisTemplate.convertAndSend("fundRaised_topic",event);
        await().atMost(3, SECONDS).untilAsserted(() -> {
            List<AnalyticsEvent> events = IterableUtils.toList(analyticsEventRepository.findAll());
            assertEquals(1, events.size());
            assertEquals(event.getUserId(), events.get(0).getReceiverId());
        });
    }

    @Container
    public static PostgreSQLContainer<?> POSTGRESQL_CONTAINER =
            new PostgreSQLContainer<>("postgres:13.6");

    @Container
    public static final RedisContainer REDIS_CONTAINER =
            new RedisContainer(DockerImageName.parse("redis/redis-stack:latest"));

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", REDIS_CONTAINER::getHost);
        registry.add("spring.data.redis.port", () -> REDIS_CONTAINER.getMappedPort(6379));

        registry.add("spring.datasource.url", POSTGRESQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRESQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRESQL_CONTAINER::getPassword);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

