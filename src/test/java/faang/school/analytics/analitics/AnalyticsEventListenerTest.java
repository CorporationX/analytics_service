package faang.school.analytics.analitics;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.redis.publisher.AnalyticsEventPublisher;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertFalse;

@SpringBootTest
@Testcontainers
class AnalyticsEventListenerTest {

    @Container
    private static final GenericContainer<?> redis = new GenericContainer<>("redis:6.2").withExposedPorts(6379);

    @Autowired
    private AnalyticsEventRepository analyticsEventRepository;

    @Autowired
    private AnalyticsEventPublisher analyticsEventPublisher;

    @Test
    void testAnalyticsEventProcessing() {

        AnalyticsEvent event = AnalyticsEvent.builder()
                .actorId(1L)
                .receiverId(2L)
                .eventType(EventType.PROFILE_VIEW)
                .receivedAt(LocalDateTime.now())
                .build();

        analyticsEventPublisher.publish(event);


        List<AnalyticsEvent> events = analyticsEventRepository.findByReceiverIdAndEventType(2L, EventType.PROFILE_VIEW).toList();
        assertFalse(events.isEmpty());
    }
}