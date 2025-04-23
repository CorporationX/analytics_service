package faang.school.analytics.listener;

import faang.school.analytics.dto.event.AnalyticDto;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AnalyticsEventSaver {
    private final AnalyticsEventService analyticsEventService;

    @Transactional
    public void saveAnalyticsEvent(AnalyticDto event, EventType eventType) {
        AnalyticsEvent analyticsEvent = AnalyticsEvent.builder()
                .eventType(eventType)
                .actorId(event.getAuthorId())
                .receiverId(event.getReceiverId())
                .receivedAt(event.getCreatedAt())
                .build();
        analyticsEventService.save(analyticsEvent);
    }
}