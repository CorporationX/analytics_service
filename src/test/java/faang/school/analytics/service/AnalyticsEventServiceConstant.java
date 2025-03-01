package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDTO;
import faang.school.analytics.dto.AnalyticsEventRequestDTO;
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
    protected static final AnalyticsEventRequestDTO ANALYTICS_EVENT_REQUEST_DTO = AnalyticsEventRequestDTO.builder()
            .receiverId(1)
            .eventType(EventType.PROFILE_VIEW)
            .from(LocalDateTime.now().minusHours(6))
            .to(LocalDateTime.now().minusHours(1))
            .build();
    protected static final AnalyticsEventRequestDTO ANALYTICS_EVENT_REQUEST_DTO2 = AnalyticsEventRequestDTO.builder()
            .receiverId(1)
            .eventType(EventType.PROFILE_VIEW)
            .from(LocalDateTime.now().minusDays(7))
            .to(LocalDateTime.now().minusDays(1))
            .build();
}
