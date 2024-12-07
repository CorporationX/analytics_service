package faang.school.analytics.config.redis;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ContextConfiguration(classes = {RedisConfigTest.class})
public class AnalyticsEventIntegrationTest {

    @Autowired
    private AnalyticsEventService analyticsEventService;

    @Autowired
    private AnalyticsEventRepository analyticsEventRepository;

    @Test
    public void testSaveAnalyticsEvent() {
        AnalyticsEvent event = AnalyticsEvent.builder()
                .receiverId(1L)
                .actorId(2L)
                .eventType(EventType.POST_LIKE)
                .receivedAt(LocalDateTime.now())
                .build();

        analyticsEventService.saveEvent(event);

        Optional<AnalyticsEvent> savedEvent = analyticsEventRepository.findById(event.getId());
        assertThat(savedEvent).isPresent();
        assertThat(savedEvent.get().getReceiverId()).isEqualTo(1L);
        assertThat(savedEvent.get().getActorId()).isEqualTo(2L);
        assertThat(savedEvent.get().getEventType()).isEqualTo(EventType.POST_LIKE);
    }
}
