package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDTO;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;

import java.time.LocalDateTime;
import java.util.stream.Stream;

public class AnalyticsEventServiceConstant {
    protected static final AnalyticsEvent EVENT = AnalyticsEvent.builder()
            .actorId(2)
            .receiverId(1)
            .eventType(EventType.PROFILE_VIEW)
            .receivedAt(LocalDateTime.now().minusMinutes(1))
            .build();

    protected static final AnalyticsEvent EVENT2 = AnalyticsEvent.builder()
            .actorId(3)
            .receiverId(1)
            .eventType(EventType.PROFILE_VIEW)
            .receivedAt(LocalDateTime.now().minusHours(2))
            .build();

    protected static final AnalyticsEvent EVENT3 = AnalyticsEvent.builder()
            .actorId(5)
            .receiverId(1)
            .eventType(EventType.PROFILE_VIEW)
            .receivedAt(LocalDateTime.now().minusHours(4))
            .build();

    protected static final Stream<AnalyticsEvent> EVENTS = Stream.of(EVENT, EVENT2, EVENT3);
    protected static final Stream<AnalyticsEvent> EVENTS2 = Stream.of(EVENT, EVENT2);

    protected static final AnalyticsEventDTO ANALYTICS_EVENT_DTO = AnalyticsEventDTO.builder()
            .id(1)
            .eventType(EventType.PROFILE_VIEW)
            .receiverId(5)
            .build();
}
