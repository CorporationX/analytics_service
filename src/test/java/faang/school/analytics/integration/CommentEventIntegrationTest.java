package faang.school.analytics.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.config.TestContainersConfig;
import faang.school.analytics.dto.comment.CommentEvent;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
@ContextConfiguration(classes = TestContainersConfig.class)
public class CommentEventIntegrationTest {
    private static final long AWAIT_TIMEOUT_SECONDS = 5;
    private static final String REDIS_CHANNEL_COMMENT = "spring.data.redis.channel.comment";

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private AnalyticsEventRepository analyticsEventRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Environment environment;

    @AfterEach
    public void cleanUp() {
        analyticsEventRepository.deleteAll();
        TestContainersConfig.redisContainer.stop();
    }

    @Test
    public void testCommentEventProcessing() throws Exception {
        String commentEventChannel = environment.getProperty(REDIS_CHANNEL_COMMENT);

        CommentEvent commentEvent = new CommentEvent(1L, 2L, 3L, LocalDateTime.now());
        String message = objectMapper.writeValueAsString(commentEvent);

        redisTemplate.convertAndSend(Objects.requireNonNull(commentEventChannel), message);

        await().atMost(AWAIT_TIMEOUT_SECONDS, TimeUnit.SECONDS).untilAsserted(() -> {
            List<AnalyticsEvent> events = analyticsEventRepository.findAll();
            assertThat(events).hasSize(1);

            AnalyticsEvent event = events.get(0);
            assertThat(event.getReceiverId()).isEqualTo(commentEvent.getCommentId());
            assertThat(event.getActorId()).isEqualTo(commentEvent.getAuthorId());
            assertThat(event.getEventType()).isEqualTo(EventType.POST_COMMENT);
        });
    }
}