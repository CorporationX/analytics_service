package faang.school.analytics.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.config.TestContainersConfig;
import faang.school.analytics.dto.user.ProfileViewEventDto;
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
import static org.awaitility.Awaitility.await;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
@ContextConfiguration(classes = TestContainersConfig.class)
public class ProfileViewListEventIntegrationTest {
    private static final long AWAIT_TIMEOUT_SECONDS = 5;
    private static final String REDIS_CHANNEL_PROFILE_VIEW_LIST = "spring.data.redis.channel.profile-view-list";

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
    public void testProfileViewListEventProcessing() throws Exception {
        String profileViewListEventChannel = environment.getProperty(REDIS_CHANNEL_PROFILE_VIEW_LIST);

        List<ProfileViewEventDto> profileViewEvents = List.of(
                new ProfileViewEventDto(1L, "name", 2L, "name", LocalDateTime.now()),
                new ProfileViewEventDto(3L, "name", 4L, "name", LocalDateTime.now()),
                new ProfileViewEventDto(5L, "name", 6L, "name", LocalDateTime.now())
        );

        String message = objectMapper.writeValueAsString(profileViewEvents);

        redisTemplate.convertAndSend(Objects.requireNonNull(profileViewListEventChannel), message);

        await().atMost(AWAIT_TIMEOUT_SECONDS, TimeUnit.SECONDS).untilAsserted(() -> {
            List<AnalyticsEvent> events = analyticsEventRepository.findAll();
            assertThat(events).hasSize(3);

            assertThat(events).extracting("receiverId").containsExactlyInAnyOrder(1L, 3L, 5L);
            assertThat(events).extracting("actorId").containsExactlyInAnyOrder(2L, 4L, 6L);
            assertThat(events).allMatch(event -> event.getEventType() == EventType.PROFILE_VIEW);
        });
    }
}