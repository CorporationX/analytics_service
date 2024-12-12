package faang.school.analytics.service;


import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.redis.publisher.AnalyticsEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsService {

    private final AnalyticsEventPublisher analyticsEventPublisher;

    public void trackProfileView(long actorId, long receiverId) {
        AnalyticsEvent event = AnalyticsEvent.builder()
                .actorId(actorId)
                .receiverId(receiverId)
                .eventType(EventType.PROFILE_VIEW)
                .receivedAt(LocalDateTime.now())
                .build();

        analyticsEventPublisher.publish(event);
        log.info("Tracked profile view: Actor ID {}, Receiver ID {}", actorId, receiverId);
    }
}
