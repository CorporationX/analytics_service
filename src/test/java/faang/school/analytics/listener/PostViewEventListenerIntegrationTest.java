package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.model.dto.PostViewEvent;
import faang.school.analytics.model.entity.AnalyticsEvent;
import faang.school.analytics.model.enums.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@SpringBootTest
@ExtendWith(SpringExtension.class)
class PostViewEventListenerIntegrationTest {

    @Value("${spring.data.redis.channel.post_view_channel}")
    private String postViewChannel;

    @Container
    public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("test_db")
            .withUsername("admin")
            .withPassword("admin")
            .withInitScript("schema_for_PostViewListener.sql");

    @Container
    public static GenericContainer<?> redisContainer = new GenericContainer<>("redis:latest")
            .withExposedPorts(6379);

    @DynamicPropertySource
    static void overrideSourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
        registry.add("spring.datasource.driver-class-name", postgresContainer::getDriverClassName);
        registry.add("spring.liquibase.enabled", () -> false);
        registry.add("spring.data.redis.host", redisContainer::getHost);
        registry.add("spring.data.redis.port", redisContainer::getFirstMappedPort);
    }

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AnalyticsEventRepository analyticsEventRepository;

    @Test
    void testOnMessageIntegration() throws Exception {
        EventType eventType = EventType.POST_VIEW;
        Long postId = 10L;
        Long authorId = 11L;
        Long actorId = 12L;
        LocalDateTime localDateTime = LocalDateTime.now();
        PostViewEvent postViewEvent = new PostViewEvent(eventType, postId, authorId, actorId, localDateTime);
        String messagePayload = objectMapper.writeValueAsString(postViewEvent);

        redisTemplate.convertAndSend(postViewChannel, messagePayload);
        Thread.sleep(1000);

        AnalyticsEvent savedEntity = analyticsEventRepository.findFirstByOrderByIdDesc();

        Assertions.assertAll(
                () -> assertThat(savedEntity).isNotNull(),
                () -> assertThat(savedEntity.getEventType()).isEqualTo(EventType.POST_VIEW),
                () -> assertEquals(eventType, savedEntity.getEventType()),
                () -> assertEquals(actorId, savedEntity.getActorId()),
                () -> assertEquals(postId, savedEntity.getReceiverId())
        );
    }
}